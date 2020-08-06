package com.questnr.services.analytics;

import com.questnr.common.HashTagRankDependents;
import com.questnr.common.StartingEndingDate;
import com.questnr.model.dto.HashTagRankDTO;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.HashTagTrendData;
import com.questnr.model.entities.HashTagTrendLinearData;
import com.questnr.model.repositories.*;
import com.questnr.services.analytics.regression.LinearRegressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HashTagTrendService implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    PostVisitRepository postVisitRepository;

    @Autowired
    HashTagTrendDataRepository hashTagTrendDataRepository;

    @Autowired
    HashTagTrendLinearDataRepository hashTagTrendLinearDataRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    LinearRegressionService linearRegressionService;

    @Value("${questnr.max-trending-hash-tags}")
    private int MAX_TRENDING_HASH_TAGS;

    private Double calculateHashTagRank(HashTagRankDTO hashTagRankDTO) {
        return HashTagRankDependents.TIME_BEING_USED * hashTagRankDTO.getTotalTimeBeingUsed();
    }

    private List<HashTagTrendLinearData> addPostActionTrendLinearData(List<HashTagTrendLinearData> hashTagTrendLinearDataList, HashTagTrendData hashTagTrendData) {
        boolean foundInList = false;
        for (HashTagTrendLinearData hashTagTrendLinearData : hashTagTrendLinearDataList) {
            if (hashTagTrendLinearData.getHashTag().equals(hashTagTrendData.getHashTag())) {
                hashTagTrendLinearData.addX(Double.valueOf(hashTagTrendData.getObservedDate().getTime()));
                hashTagTrendLinearData.addY(hashTagTrendData.getRank());
                foundInList = true;
                break;
            }
        }
        if (!foundInList) {
            HashTagTrendLinearData newHashTagTrendLinearData = new HashTagTrendLinearData();
            newHashTagTrendLinearData.setHashTag(hashTagTrendData.getHashTag());
            newHashTagTrendLinearData.addX(Double.valueOf(hashTagTrendData.getObservedDate().getTime()));
            newHashTagTrendLinearData.addY(hashTagTrendData.getRank());
            hashTagTrendLinearDataList.add(newHashTagTrendLinearData);
        }
        return hashTagTrendLinearDataList;
    }

    private void calculateHashTagTrendOverTime() {
        // Can be improved this by only finding the slope for those who has new data

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(10);
        startingEndingDate.build();

        List<HashTagTrendData> hashTagTrendDataList = hashTagTrendDataRepository.findAllByObservedDateBetween(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate());

        List<HashTagTrendLinearData> hashTagTrendLinearDataArrayList = new ArrayList<>();
        for (HashTagTrendData hashTagTrendData : hashTagTrendDataList) {
            addPostActionTrendLinearData(hashTagTrendLinearDataArrayList, hashTagTrendData);
        }

        for (HashTagTrendLinearData hashTagTrendLinearData : hashTagTrendLinearDataArrayList) {
            hashTagTrendLinearData.setSlop(linearRegressionService.getSlopFromDataOverTime(hashTagTrendLinearData.getX(), hashTagTrendLinearData.getY()));

        }

        List<HashTagTrendLinearData> pass1HashTagTrendLinearDataArrayList = hashTagTrendLinearDataArrayList.stream()
                .filter(hashTagTrendLinearData ->
                        !Double.isNaN(hashTagTrendLinearData.getSlop())).collect(Collectors.toList());

        // List sorted with descending order of regression slope
        Comparator<HashTagTrendLinearData> hashTagTrendLinearDataComparator
                = Comparator.comparing(HashTagTrendLinearData::getSlop);

        pass1HashTagTrendLinearDataArrayList.sort(hashTagTrendLinearDataComparator.reversed());


        List<HashTagTrendLinearData> subListHashTagTrendLinearDataArrayList = pass1HashTagTrendLinearDataArrayList
                .subList(0, pass1HashTagTrendLinearDataArrayList.size() > MAX_TRENDING_HASH_TAGS ? MAX_TRENDING_HASH_TAGS : pass1HashTagTrendLinearDataArrayList.size());

        int trendRank = 1;
        for (HashTagTrendLinearData hashTagTrendLinearData : subListHashTagTrendLinearDataArrayList) {
            hashTagTrendLinearData.setTrendRank(trendRank++);
        }

        if(subListHashTagTrendLinearDataArrayList.size() > 0){
            hashTagTrendLinearDataRepository.deleteAll();
        }
        hashTagTrendLinearDataRepository.saveAll(subListHashTagTrendLinearDataArrayList);
    }

    public void run() {

        List<HashTag> hashTagList = hashTagRepository.findAll();


        StartingEndingDate startingEndingDate = new StartingEndingDate();
//        startingEndingDate.setDaysBefore(13);
        startingEndingDate.build();

        // Yesterday's Date
        Date datePointer = startingEndingDate.getStartingDate();

        // If this algorithm hasn't been run before for this day.
        if (hashTagTrendDataRepository.countByObservedDate(datePointer) == 0) {
//            while (datePointer.getTime() < startingEndingDate.getEndingDate().getTime()) {
                LOGGER.info("HashTag Trending Algorithm Started!");

                // Get day + 1
                Calendar c = Calendar.getInstance();
                c.setTime(datePointer);
                c.add(Calendar.DATE, 1);

                // Today's Date
                Date nextDatePointer = c.getTime();
                Long totalTimeBeingUsed;

                for (HashTag hashTag : hashTagList) {

                    totalTimeBeingUsed = postActionRepository.countAllByHashTagsAndCreatedAtBetween(hashTag, datePointer, nextDatePointer);

                    if (totalTimeBeingUsed <= HashTagRankDependents.TIME_BEING_USED_THRESHOLD)
                        continue;

                    HashTagRankDTO hashTagRankDTO = new HashTagRankDTO();
                    hashTagRankDTO.setHashTagId(hashTag.getHashTagId());
                    hashTagRankDTO.setTotalTimeBeingUsed(totalTimeBeingUsed);
                    hashTagRankDTO.setRank(this.calculateHashTagRank(hashTagRankDTO));
                    hashTagRankDTO.setDate(datePointer);

                    if (hashTagRankDTO.getRank() > HashTagRankDependents.MIN_THRESHOLD) {
                        HashTagTrendData hashTagTrendData = new HashTagTrendData();
                        hashTagTrendData.setHashTag(hashTag);
                        hashTagTrendData.setObservedDate(datePointer);
                        hashTagTrendData.setRank(hashTagRankDTO.getRank());
                        hashTagTrendDataRepository.save(hashTagTrendData);
                    }

                }

                this.calculateHashTagTrendOverTime();

//                datePointer = nextDatePointer;
                LOGGER.info("Post Action Trending Algorithm Completed!");
        }
        else {
            LOGGER.info("Post Action Trending Algorithm Ignored...");
        }
    }

}
