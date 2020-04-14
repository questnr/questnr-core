package com.questnr.services;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import com.questnr.common.CommunityRankDependents;
import com.questnr.common.CommunityTrendLinearData;
import com.questnr.common.StartingEndingDate;
import com.questnr.model.dto.CommunityRankDTO;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.*;
import com.questnr.util.LinearRegression;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {
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

    private Double calculateCommunityRank(CommunityRankDTO communityRankDTO) {
        return CommunityRankDependents.USER_FOLLOWERS * communityRankDTO.getTotalFollowers() +
                CommunityRankDependents.POST_ACTION * communityRankDTO.getTotalPosts() +
                CommunityRankDependents.LIKE_ACTION * communityRankDTO.getTotalLikes() +
                CommunityRankDependents.COMMENT_ACTION * communityRankDTO.getTotalComments() +
                CommunityRankDependents.POST_VISIT * communityRankDTO.getTotalPostVisits() +
                CommunityRankDependents.PROFILE_VISIT * communityRankDTO.getTotalVisits();
    }

    private List<CommunityTrendLinearData> addCommunityTrendLinearData(List<CommunityTrendLinearData> communityTrendLinearDataList, CommunityTrendData communityTrendData){
        boolean foundInList = false;
        for(CommunityTrendLinearData communityTrendLinearData : communityTrendLinearDataList){
            if(communityTrendLinearData.getCommunity().equals(communityTrendData.getCommunity())){
                communityTrendLinearData.addX(Double.valueOf(communityTrendData.getObservedDate().getTime()));
                communityTrendLinearData.addY(communityTrendData.getRank());
                foundInList = true;
                break;
            }
        }
        if(!foundInList){
            CommunityTrendLinearData newCommunityTrendLinearData = new CommunityTrendLinearData();
            newCommunityTrendLinearData.setCommunity(communityTrendData.getCommunity());
            newCommunityTrendLinearData.addX(Double.valueOf(communityTrendData.getObservedDate().getTime()));
            newCommunityTrendLinearData.addY(communityTrendData.getRank());
            communityTrendLinearDataList.add(newCommunityTrendLinearData);
        }
        return communityTrendLinearDataList;
    }

    private void calculateCommunityTrendOverTime(){
//        Comparator<CommunityTrendData> communityTrendDataComparator
//                = Comparator.comparing(CommunityTrendData::getObservedDate);
//        communityTrendDataList.sort(communityTrendDataComparator.reversed());

        List<CommunityTrendData> communityTrendDataList = communityTrendDataRepository.findAll();

        List<CommunityTrendLinearData> communityTrendLinearDataList = new ArrayList<>();
        for(CommunityTrendData communityTrendData: communityTrendDataList){
            communityTrendLinearDataList = addCommunityTrendLinearData(communityTrendLinearDataList, communityTrendData);
        }

        for(CommunityTrendLinearData communityTrendLinearData : communityTrendLinearDataList){
            double[] xAxis = new double[communityTrendLinearData.getX().size()];
            double[] yAxis = new double[communityTrendLinearData.getY().size()];
            for (int n = 0; n < communityTrendLinearData.getX().size(); n++) {
                xAxis[n] = communityTrendLinearData.getX().get(n);
            }
            for (int n = 0; n < communityTrendLinearData.getY().size(); n++) {
                yAxis[n] = communityTrendLinearData.getY().get(n);
            }
            LinearRegression linearRegression = new LinearRegression(xAxis, yAxis);
            communityTrendLinearData.setSlop(linearRegression.slope());

            communityTrendLinearDataRepository.save(communityTrendLinearData);
        }
    }

    @Scheduled(fixedRate = (3600 * 24 * 1000))
    public void storeCommunityTrendData() {
//        communityTrendDataRepository.deleteAll();
        LOGGER.info("Community Trending Algorithm Started!");

        List<Community> communityList = communityRepository.findAll();

        List<CommunityTrendData> communityTrendDataList = new ArrayList<>();

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(40);
        startingEndingDate.build();

        Date datePointer = startingEndingDate.getStartingDate();

        while (datePointer.getTime() < startingEndingDate.getEndingDate().getTime()) {
            // Get day + 1
            Calendar c = Calendar.getInstance();
            c.setTime(datePointer);
            c.add(Calendar.DATE, 1);
            Date nextDatePointer = c.getTime();

            for (Community community : communityList) {

                List<CommunityUser> communityUserList = communityUserRepository.findAllByCommunityAndCreatedAtBetween(community, datePointer, nextDatePointer);
                List<User> userList = communityUserList.stream().map(communityUser ->
                        communityUser.getUser()
                ).collect(Collectors.toList());

                List<PostAction> postActionList = postActionRepository.findAllByCommunityAndCreatedAtBetween(community, datePointer, nextDatePointer);

                if(postActionList.size() <= 0 ) continue;

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

                CommunityRankDTO communityRankDTO = new CommunityRankDTO();
                communityRankDTO.setTotalFollowers(Long.valueOf(userList.size()));
                communityRankDTO.setTotalPosts(Long.valueOf(postActionList.size()));
                communityRankDTO.setTotalLikes(Long.valueOf(totalLikes));
                communityRankDTO.setTotalComments(Long.valueOf(totalComments));
                communityRankDTO.setTotalPostVisits(Long.valueOf(totalPostVisits));
                communityRankDTO.setTotalVisits(Long.valueOf(0));
                communityRankDTO.setRank(this.calculateCommunityRank(communityRankDTO));
                communityRankDTO.setDate(datePointer);

                if(communityRankDTO.getRank() > CommunityRankDependents.MIN_THRESHOLD) {
                    CommunityTrendData communityTrendData = new CommunityTrendData();
                    communityTrendData.setCommunity(community);
                    communityTrendData.setObservedDate(datePointer);
                    communityTrendData.setRank(communityRankDTO.getRank());
                    communityTrendDataRepository.save(communityTrendData);
                    communityTrendDataList.add(communityTrendData);
                }

            }

            datePointer = nextDatePointer;
        }
        this.calculateCommunityTrendOverTime();

        LOGGER.info("Community Trending Algorithm Completed! Decided trend xx community");
    }


    public void writeCommunityTrends(List<CommunityRankDTO> communityTrendData) throws IOException {
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

//    @Scheduled(fixedRate = (3600 * 24 * 1000))
//    public void communityTrend(){
//        LOGGER.info("Community Trending Algorithm Started!");
//
//        StartingEndingDate startingEndingDate = new StartingEndingDate();
//        startingEndingDate.setDaysBefore(28);
//        startingEndingDate.build();
//
////        List<CommunityOverTime> communityOverTimeList = new ArrayList<>();
////
////        List<Long> communityIdList = new ArrayList<>();
//
//        Date datePointer = startingEndingDate.getStartingDate();
//        while(datePointer.getTime() < startingEndingDate.getStartingDate().getTime()){
//            List<Object[]> objects = communityRepository.findByCommunityByOnDate(datePointer);
//            for(Object[] object: objects) {
//                Long communityId = Long.valueOf(object[0].toString());
//
//                CommunityRankDTO communityRankDTO = new CommunityRankDTO();
//                communityRankDTO.setTotalFollowers(Long.valueOf(object[1].toString()));
//                communityRankDTO.setTotalPosts(Long.valueOf(object[2].toString()));
//                communityRankDTO.setTotalLikes(Long.valueOf(object[3].toString()));
//                communityRankDTO.setTotalComments(Long.valueOf(object[4].toString()));
//                communityRankDTO.setTotalPostVisits(Long.valueOf(object[5].toString()));
//                communityRankDTO.setTotalVisits(Long.valueOf(0));
//                communityRankDTO.setRank(this.calculateCommunityRank(communityRankDTO));
//                communityRankDTO.setDate(datePointer);
//
//
////                if(communityIdList.contains(communityId)){
////                    communityOverTimeList.stream().map(communityOverTime -> {
////                        if(communityOverTime.getCommunityId().equals(communityId)){
////                            communityOverTime.addCommunityRankDTO(communityRankDTO);
////                        }
////                        return null;
////                    });
////                }else{
////                    CommunityOverTime communityOverTime = new CommunityOverTime();
////                    communityOverTime.setCommunityId(communityId);
////                    communityOverTime.addCommunityRankDTO(communityRankDTO);
////                    communityOverTimeList.add(communityOverTime);
////                    communityIdList.add(communityId);
////                }
//
//                CommunityTrendData communityTrendData = new CommunityTrendData();
//                communityTrendData.setCommunity(communityRepository.findByCommunityId(communityId));
//                communityTrendData.setObservedDate(datePointer);
//                communityTrendData.setRank(communityRankDTO.getRank());
//                communityTrendDataRepository.save(communityTrendData);
//
//            }
//
//
//            // Get day + 1
//            Calendar c = Calendar.getInstance();
//            c.setTime(datePointer);
//            c.add(Calendar.DATE, 1);
//            datePointer = c.getTime();
//        }
//
//        LOGGER.info("Community Trending Algorithm Completed! Decided trend xx community");
//    }


//    @Scheduled(fixedRate = (3600 * 24 * 1000))
//    public void communityTrend(){
//        LOGGER.info("Community Trending Algorithm Started!");
//
//        StartingEndingDate startingEndingDate = new StartingEndingDate();
//        startingEndingDate.setDaysBefore(28);
//        startingEndingDate.build();
//
//        List<Community> communityList = communityRepository.findAll();
//        List<CommunityRankDTO> communityRankDTOList = new ArrayList<>();
//
//        for(Community community: communityList){
//
//            CommunityRankDTO communityRankDTO = new CommunityRankDTO();
//            communityRankDTO.setCommunityId(community.getCommunityId());
//
//
//            Integer totalFollowers = community.getUsers().size();
//            Integer totalPosts = community.getPostActionSet().size();
//            Integer totalLikes = 0;
//            for(PostAction postAction : community.getPostActionSet()) {
//                totalLikes += postAction.getLikeActionSet().size();
//            }
//            Integer totalComments = 0;
//            for(PostAction postAction : community.getPostActionSet()) {
//                totalComments += postAction.getCommentActionSet().size();
//            }
//            Integer totalPostVisits = 0;
//            for(PostAction postAction : community.getPostActionSet()) {
//                totalPostVisits += postAction.getPostVisitSet().size();
//            }
//            Integer totalVisits = 0;
//
//            Double communityRank = CommunityRankDependents.USER_FOLLOWERS * totalFollowers +
//                    CommunityRankDependents.POST_ACTION * totalPosts +
//                    CommunityRankDependents.LIKE_ACTION * totalLikes +
//                    CommunityRankDependents.COMMENT_ACTION * totalComments +
//                    CommunityRankDependents.POST_VISIT * totalPostVisits +
//                    CommunityRankDependents.PROFILE_VISIT * totalVisits;
//
//            // If community is below the threshold limit.
//            if(communityRank < CommunityRankDependents.MIN_THRESHOLD){
//                continue;
//            }
//
//            communityRankDTO.setTotalFollowers(Long.valueOf(totalFollowers));
//            communityRankDTO.setTotalPosts(Long.valueOf(totalPosts));
//            communityRankDTO.setTotalLikes(Long.valueOf(totalLikes));
//            communityRankDTO.setTotalComments(Long.valueOf(totalComments));
//            communityRankDTO.setTotalPostVisits(Long.valueOf(totalPostVisits));
//            communityRankDTO.setTotalVisits(Long.valueOf(totalVisits));
//            communityRankDTO.setRank(communityRank);
//            communityRankDTOList.add(communityRankDTO);
//        }
//        Comparator<CommunityRankDTO> communityTrendDataComparator
//                = Comparator.comparing(CommunityRankDTO::getRank);
//        communityRankDTOList.sort(communityTrendDataComparator.reversed());
//
//
//        this.writeCommunityTrends(new PrintWriter(System.out), communityRankDTOList);
//        LOGGER.info("Community Trending Algorithm Completed! Decided trend "+ communityRankDTOList.size()+" community");
//    }
}
