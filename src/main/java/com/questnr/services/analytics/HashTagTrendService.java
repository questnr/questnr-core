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
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class HashTagTrendService {
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

    private void calculatePostActionTrendOverTime() {
        // Can be improved this by only finding the slope for those who has new data

        hashTagTrendLinearDataRepository.deleteAll();


        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(40);
        startingEndingDate.build();

        List<HashTagTrendData> hashTagTrendDataList = hashTagTrendDataRepository.findAllByObservedDateBetween(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate());

        List<HashTagTrendLinearData> hashTagTrendLinearDataArrayList = new ArrayList<>();
        for (HashTagTrendData hashTagTrendData : hashTagTrendDataList) {
            // @Todo: Test if this is working
//            postActionTrendLinearDataList = addPostActionTrendLinearData(postActionTrendLinearDataList, postActionTrendData);
            addPostActionTrendLinearData(hashTagTrendLinearDataArrayList, hashTagTrendData);
        }

        for (HashTagTrendLinearData hashTagTrendLinearData : hashTagTrendLinearDataArrayList) {
            hashTagTrendLinearData.setSlop(linearRegressionService.getSlopFromDataOverTime(hashTagTrendLinearData.getX(), hashTagTrendLinearData.getY()));

            hashTagTrendLinearDataRepository.save(hashTagTrendLinearData);
        }
    }

    public void run() {

        List<HashTag> hashTagList = hashTagRepository.findAll();


        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(45);
        startingEndingDate.build();

        // Yesterday's Date
        Date datePointer = startingEndingDate.getStartingDate();

        // If this algorithm hasn't been run before for this day.
//        if (hashTagTrendDataRepository.countByObservedDate(datePointer) == 0) {
        while(datePointer.getTime() < startingEndingDate.getEndingDate().getTime()){
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

            this.calculatePostActionTrendOverTime();

            datePointer = nextDatePointer;
            LOGGER.info("Post Action Trending Algorithm Completed!");
        }
//        else {
//            LOGGER.info("Post Action Trending Algorithm Ignored...");
//        }
    }

}
