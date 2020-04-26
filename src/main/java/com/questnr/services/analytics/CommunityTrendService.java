package com.questnr.services.analytics;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import com.questnr.common.CommunityRankDependents;
import com.questnr.common.StartingEndingDate;
import com.questnr.model.dto.CommunityRankDTO;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.*;
import com.questnr.services.analytics.regression.LinearRegressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommunityTrendService implements Runnable{
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
    CommunityTrendDataRepository communityTrendDataRepository;

    @Autowired
    CommunityTrendLinearDataRepository communityTrendLinearDataRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    LinearRegressionService linearRegressionService;

    private Double calculateCommunityRank(CommunityRankDTO communityRankDTO) {
        return CommunityRankDependents.USER_FOLLOWERS * communityRankDTO.getTotalFollowers() +
                CommunityRankDependents.POST_ACTION * communityRankDTO.getTotalPosts() +
                CommunityRankDependents.LIKE_ACTION * communityRankDTO.getTotalLikes() +
                CommunityRankDependents.COMMENT_ACTION * communityRankDTO.getTotalComments() +
                CommunityRankDependents.POST_VISIT * communityRankDTO.getTotalPostVisits() +
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

        communityTrendLinearDataRepository.deleteAll();

//        Comparator<CommunityTrendData> communityTrendDataComparator
//                = Comparator.comparing(CommunityTrendData::getObservedDate);
//        communityTrendDataList.sort(communityTrendDataComparator.reversed());

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(40);
        startingEndingDate.build();

        List<CommunityTrendData> communityTrendDataList = communityTrendDataRepository.findAllByObservedDateBetween(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate());

        List<CommunityTrendLinearData> communityTrendLinearDataList = new ArrayList<>();
        for (CommunityTrendData communityTrendData : communityTrendDataList) {
            // @Todo: Test if this is working
            // communityTrendLinearDataList = addCommunityTrendLinearData(communityTrendLinearDataList, communityTrendData);
            addCommunityTrendLinearData(communityTrendLinearDataList, communityTrendData);
        }

        for (CommunityTrendLinearData communityTrendLinearData : communityTrendLinearDataList) {
            communityTrendLinearData.setSlop(linearRegressionService.getSlopFromDataOverTime(communityTrendLinearData.getX(), communityTrendLinearData.getY()));

            communityTrendLinearDataRepository.save(communityTrendLinearData);
        }
    }

    public void run() {

        List<Community> communityList = communityRepository.findAll();


        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.build();

        // Yesterday's Date
        Date datePointer = startingEndingDate.getStartingDate();

        // If this algorithm hasn't been run before for this day.
        if (communityTrendDataRepository.countByObservedDate(datePointer) == 0) {
            LOGGER.info("Community Trending Algorithm Started!");

            // Get day + 1
            Calendar c = Calendar.getInstance();
            c.setTime(datePointer);
            c.add(Calendar.DATE, 1);

            // Today's Date
            Date nextDatePointer = c.getTime();

            for (Community community : communityList) {

                List<CommunityUser> communityUserList = communityUserRepository.findAllByCommunityAndCreatedAtBetween(community, datePointer, nextDatePointer);
                List<User> userList = communityUserList.stream().map(communityUser ->
                        communityUser.getUser()
                ).collect(Collectors.toList());

                int totalFollowers = userList.size();
                List<PostAction> postActionList = postActionRepository.findAllByCommunityAndCreatedAtBetween(community, datePointer, nextDatePointer);

                int totalPosts = postActionList.size();
                if (totalPosts <= 0) continue;

                Long totalLikes = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalLikes += likeActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }

                Long totalComments = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalComments += commentActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }
                Long totalPostVisits = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalPostVisits += postVisitRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }

                int totalVisits = 0;

                if (totalFollowers <= CommunityRankDependents.USER_FOLLOWER_COUNT_THRESHOLD
                        && totalPosts <= CommunityRankDependents.POST_COUNT_THRESHOLD
                        && totalLikes <= CommunityRankDependents.LIKE_COUNT_THRESHOLD
                        && totalComments <= CommunityRankDependents.COMMENT_COUNT_THRESHOLD
                        && totalPostVisits <= CommunityRankDependents.POST_VISIT_COUNT_THRESHOLD
                        && totalVisits <= CommunityRankDependents.VISIT_COUNT_THRESHOLD)
                    continue;

                CommunityRankDTO communityRankDTO = new CommunityRankDTO();
                communityRankDTO.setTotalFollowers(Long.valueOf(totalFollowers));
                communityRankDTO.setTotalPosts(Long.valueOf(totalPosts));
                communityRankDTO.setTotalLikes(Long.valueOf(totalLikes));
                communityRankDTO.setTotalComments(Long.valueOf(totalComments));
                communityRankDTO.setTotalPostVisits(Long.valueOf(totalPostVisits));
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

            LOGGER.info("Community Trending Algorithm Completed! Decided trend xx community");
        } else {
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
