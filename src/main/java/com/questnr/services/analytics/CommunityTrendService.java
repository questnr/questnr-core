package com.questnr.services.analytics;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import com.questnr.common.CommunityRankDependents;
import com.questnr.common.StartingEndingDate;
import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.model.dto.community.CommunityRankDTO;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.*;
import com.questnr.services.analytics.regression.LinearRegressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommunityTrendService implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityUserRepository communityUserRepository;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    LikeActionRepository likeActionRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    PostVisitRepository postVisitRepository;

    @Autowired
    SharePostActionRepository sharePostActionRepository;

    @Autowired
    CommunityTrendDataRepository communityTrendDataRepository;

    @Autowired
    CommunityTrendLinearDataRepository communityTrendLinearDataRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    LinearRegressionService linearRegressionService;

    @Value("${questnr.max-trending-communities}")
    private int MAX_TRENDING_COMMUNITIES;

    private Double calculateCommunityRank(CommunityRankDTO communityRankDTO) {
        return CommunityRankDependents.USER_FOLLOWERS * communityRankDTO.getTotalFollowers() +
                CommunityRankDependents.POST_ACTION * communityRankDTO.getTotalPosts() +
                CommunityRankDependents.LIKE_ACTION * communityRankDTO.getTotalLikes() +
                CommunityRankDependents.COMMENT_ACTION * communityRankDTO.getTotalComments() +
                CommunityRankDependents.POST_VISIT * communityRankDTO.getTotalPostVisits() +
                CommunityRankDependents.POST_SHARED * communityRankDTO.getTotalPostShared() +
                CommunityRankDependents.PROFILE_VISIT * communityRankDTO.getTotalVisits();
    }

    private List<CommunityTrendLinearData> addCommunityTrendLinearData(List<CommunityTrendLinearData> communityTrendLinearDataList, CommunityTrendData communityTrendData) {
        boolean foundInList = false;
        for (CommunityTrendLinearData communityTrendLinearData : communityTrendLinearDataList) {
            if (communityTrendLinearData.getCommunity().equals(communityTrendData.getCommunity())) {
                communityTrendLinearData.addX(Double.valueOf(communityTrendData.getObservedDate().getTime()));
                communityTrendLinearData.addY(communityTrendData.getRank());
                foundInList = true;
                break;
            }
        }
        if (!foundInList) {
            CommunityTrendLinearData newCommunityTrendLinearData = new CommunityTrendLinearData();
            newCommunityTrendLinearData.setCommunity(communityTrendData.getCommunity());
            newCommunityTrendLinearData.addX(Double.valueOf(communityTrendData.getObservedDate().getTime()));
            newCommunityTrendLinearData.addY(communityTrendData.getRank());
            communityTrendLinearDataList.add(newCommunityTrendLinearData);
        }
        return communityTrendLinearDataList;
    }

    private void calculateCommunityTrendOverTime() {
        // Can be improved this by only finding the slope for those who has new data

//        Comparator<CommunityTrendData> communityTrendDataComparator
//                = Comparator.comparing(CommunityTrendData::getObservedDate);
//        communityTrendDataList.sort(communityTrendDataComparator.reversed());

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(10);
        startingEndingDate.build();

        List<CommunityTrendData> communityTrendDataList = communityTrendDataRepository.findAllByObservedDateBetween(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate());

        List<CommunityTrendLinearData> communityTrendLinearDataList = new ArrayList<>();
        for (CommunityTrendData communityTrendData : communityTrendDataList) {
            addCommunityTrendLinearData(communityTrendLinearDataList, communityTrendData);
        }

        for (CommunityTrendLinearData communityTrendLinearData : communityTrendLinearDataList) {
            communityTrendLinearData.setSlop(linearRegressionService.getSlopFromDataOverTime(communityTrendLinearData.getX(), communityTrendLinearData.getY()));
        }

        List<CommunityTrendLinearData> pass1CommunityTrendLinearDataList = communityTrendLinearDataList.stream()
                .filter(communityTrendLinearData ->
                        !Double.isNaN(communityTrendLinearData.getSlop())).collect(Collectors.toList());

        // List sorted with descending order of regression slope
        Comparator<CommunityTrendLinearData> communityTrendLinearDataComparator
                = Comparator.comparing(CommunityTrendLinearData::getSlop);

        pass1CommunityTrendLinearDataList.sort(communityTrendLinearDataComparator.reversed());

        List<CommunityTrendLinearData> subListCommunityTrendLinearDataList = pass1CommunityTrendLinearDataList
                .subList(0, pass1CommunityTrendLinearDataList.size() > MAX_TRENDING_COMMUNITIES ? MAX_TRENDING_COMMUNITIES : pass1CommunityTrendLinearDataList.size());

        int trendRank = 1;
        for (CommunityTrendLinearData communityTrendLinearData : subListCommunityTrendLinearDataList) {
            communityTrendLinearData.setTrendRank(trendRank++);
        }

        if (subListCommunityTrendLinearDataList.size() > 0) {
            communityTrendLinearDataRepository.deleteAll();
        }
        communityTrendLinearDataRepository.saveAll(subListCommunityTrendLinearDataList);
    }

    public void run() {
        StartingEndingDate startingEndingDate = new StartingEndingDate();
//        startingEndingDate.setDaysBefore(4);
        startingEndingDate.build();

        // Yesterday's Date
        Date datePointer = startingEndingDate.getStartingDate();

        // If this algorithm hasn't been run before for this day.
        if (communityTrendDataRepository.countByObservedDate(datePointer) == 0) {
//        while (datePointer.getTime() < startingEndingDate.getEndingDate().getTime()) {
            LOGGER.info("Community Trending Algorithm Started!");

            // Only public communities are allowed for Trending Features.
            List<Community> communityList = communityRepository.findAllByCommunity_CommunityPrivacy(CommunityPrivacy.pub);

            // Get day + 1
            Calendar c = Calendar.getInstance();
            c.setTime(datePointer);
            c.add(Calendar.DATE, 1);

            // Today's Date
            Date nextDatePointer = c.getTime();

            for (Community community : communityList) {

                List<CommunityUser> communityUserList = communityUserRepository.findAllByCommunityAndCreatedAtBetween(community,
                        datePointer,
                        nextDatePointer);

                int totalFollowers = communityUserList.size();
//                if (totalFollowers < CommunityRankDependents.USER_FOLLOWER_COUNT_THRESHOLD) continue;
                List<PostAction> postActionList = postActionRepository.findAllByCommunityAndCreatedAtBetween(community, datePointer, nextDatePointer);

                int totalPosts = postActionList.size();
//                if (totalPosts < CommunityRankDependents.POST_COUNT_THRESHOLD) continue;

                Long totalLikes = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalLikes += likeActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }

                if (totalLikes < CommunityRankDependents.LIKE_COUNT_THRESHOLD
                && totalFollowers < CommunityRankDependents.USER_FOLLOWER_COUNT_THRESHOLD
                && totalPosts < CommunityRankDependents.POST_COUNT_THRESHOLD)
                    continue;

                Long totalComments = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalComments += commentActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }
                if (totalComments < CommunityRankDependents.COMMENT_COUNT_THRESHOLD)
                    continue;

                Long totalPostVisits = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalPostVisits += postVisitRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }
                if (totalPostVisits < CommunityRankDependents.POST_VISIT_COUNT_THRESHOLD)
                    continue;

                Long totalPostShared = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalPostShared += sharePostActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }
                if (totalPostShared < CommunityRankDependents.POST_SHARED_COUNT_THRESHOLD)
                    continue;

//                int totalVisits = 0;
//                if(totalVisits < CommunityRankDependents.VISIT_COUNT_THRESHOLD)
//                    continue;

                CommunityRankDTO communityRankDTO = new CommunityRankDTO();
                communityRankDTO.setTotalFollowers(Long.valueOf(totalFollowers));
                communityRankDTO.setTotalPosts(Long.valueOf(totalPosts));
                communityRankDTO.setTotalLikes(totalLikes);
                communityRankDTO.setTotalComments(totalComments);
                communityRankDTO.setTotalPostVisits(totalPostVisits);
                communityRankDTO.setTotalPostShared(totalPostShared);
                communityRankDTO.setTotalVisits(Long.valueOf(0));
                communityRankDTO.setRank(this.calculateCommunityRank(communityRankDTO));
                communityRankDTO.setDate(datePointer);

                if (communityRankDTO.getRank() > CommunityRankDependents.MIN_THRESHOLD) {
                    CommunityTrendData communityTrendData = new CommunityTrendData();
                    communityTrendData.setCommunity(community);
                    communityTrendData.setObservedDate(datePointer);
                    communityTrendData.setRank(communityRankDTO.getRank());
                    communityTrendDataRepository.save(communityTrendData);
                }

            }

            this.calculateCommunityTrendOverTime();

//            datePointer = nextDatePointer;
            LOGGER.info("Community Trending Algorithm Completed! Decided trend xx community");
        }
        else{
            LOGGER.info("Community Trending Algorithm Ignored...");
        }
    }


    private void writeCommunityTrends(List<CommunityRankDTO> communityTrendData) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:community-trend.csv");
        File file = resource.getFile();

        Writer writer = new FileWriter(file);

        try {

            ColumnPositionMappingStrategy<CommunityRankDTO> mapStrategy
                    = new ColumnPositionMappingStrategy<>();

            mapStrategy.setType(CommunityRankDTO.class);

            String[] columns = new String[]{"communityId", "totalFollowers", "totalPosts", "totalLikes", "totalComments", "totalVisits", "totalPostVisits", "rank"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<CommunityRankDTO> btcsv = new StatefulBeanToCsvBuilder<CommunityRankDTO>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mapStrategy)
                    .withSeparator(',')
                    .build();

            btcsv.write(communityTrendData);


        } catch (CsvException ex) {
            LOGGER.error("Error mapping Bean to CSV", ex);
        }
    }
}
