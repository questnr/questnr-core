package com.questnr.services.analytics;

import com.questnr.common.StartingEndingDate;
import com.questnr.common.UserRankDependents;
import com.questnr.model.dto.UserRankDTO;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.*;
import com.questnr.services.analytics.regression.LinearRegressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserTrendService implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserFollowerRepository userFollowerRepository;

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
    UserTrendDataRepository userTrendDataRepository;

    @Autowired
    UserTrendLinearDataRepository userTrendLinearDataRepository;

    @Autowired
    LinearRegressionService linearRegressionService;

    @Value("${questnr.max-trending-users}")
    private int MAX_TRENDING_USERS;

    private Double calculateUserRank(UserRankDTO userRankDTO) {
        return UserRankDependents.USER_FOLLOWERS * userRankDTO.getTotalFollowers() +
                UserRankDependents.POST_ACTION * userRankDTO.getTotalPosts() +
                UserRankDependents.LIKE_ACTION * userRankDTO.getTotalLikes() +
                UserRankDependents.COMMENT_ACTION * userRankDTO.getTotalComments() +
                UserRankDependents.POST_VISIT * userRankDTO.getTotalPostVisits() +
                UserRankDependents.POST_SHARED * userRankDTO.getTotalPostShared() +
                UserRankDependents.PROFILE_VISIT * userRankDTO.getTotalVisits();
    }

    private List<UserTrendLinearData> addUserTrendLinearData(List<UserTrendLinearData> userTrendLinearDataList, UserTrendData userTrendData) {
        boolean foundInList = false;
        for (UserTrendLinearData userTrendLinearData : userTrendLinearDataList) {
            if (userTrendLinearData.getUser().equals(userTrendData.getUser())) {
                userTrendLinearData.addX(Double.valueOf(userTrendData.getObservedDate().getTime()));
                userTrendLinearData.addY(userTrendData.getRank());
                userTrendLinearData.addPoints(userTrendData.getRank());
                foundInList = true;
                break;
            }
        }
        if (!foundInList) {
            UserTrendLinearData newUserTrendLinearData = new UserTrendLinearData();
            newUserTrendLinearData.setUser(userTrendData.getUser());
            newUserTrendLinearData.addX(Double.valueOf(userTrendData.getObservedDate().getTime()));
            newUserTrendLinearData.addY(userTrendData.getRank());
            newUserTrendLinearData.setPoints(userTrendData.getRank());
            userTrendLinearDataList.add(newUserTrendLinearData);
        }
        return userTrendLinearDataList;
    }

    private void calculateUserTrendOverTime() {
        // Can be improved this by only finding the slope for those who has new data

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(40);
        startingEndingDate.build();

        List<UserTrendData> userTrendDataList = userTrendDataRepository.findAllByObservedDateBetween(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate());

        List<UserTrendLinearData> userTrendLinearDataList = new ArrayList<>();
        for (UserTrendData userTrendData : userTrendDataList) {
            addUserTrendLinearData(userTrendLinearDataList, userTrendData);
        }

        for (UserTrendLinearData userTrendLinearData : userTrendLinearDataList) {
            userTrendLinearData.setSlop(linearRegressionService.getSlopFromDataOverTime(userTrendLinearData.getX(), userTrendLinearData.getY()));
        }

        List<UserTrendLinearData> pass1UserTrendLinearDataList = userTrendLinearDataList.stream()
                .filter(userTrendLinearData ->
                        !Double.isNaN(userTrendLinearData.getSlop())).collect(Collectors.toList());

        // List sorted with descending order of regression slope
        Comparator<UserTrendLinearData> userTrendLinearDataComparator
                = Comparator.comparing(UserTrendLinearData::getSlop);

        pass1UserTrendLinearDataList.sort(userTrendLinearDataComparator.reversed());

        List<UserTrendLinearData> subListUserTrendLinearDataList = pass1UserTrendLinearDataList
                .subList(0, pass1UserTrendLinearDataList.size() > MAX_TRENDING_USERS ? MAX_TRENDING_USERS : pass1UserTrendLinearDataList.size());

        int trendRank = 1;
        for (UserTrendLinearData userTrendLinearData : subListUserTrendLinearDataList) {
            userTrendLinearData.setTrendRank(trendRank++);
        }

        if (subListUserTrendLinearDataList.size() > 0) {
            userTrendLinearDataRepository.deleteAll();
        }
        userTrendLinearDataRepository.saveAll(subListUserTrendLinearDataList);
    }

    public void run() {

        List<User> userList = userRepository.findAll();


        StartingEndingDate startingEndingDate = new StartingEndingDate();
//        startingEndingDate.setDaysBefore(5);
        startingEndingDate.build();

        // Yesterday's Date
        Date datePointer = startingEndingDate.getStartingDate();

        // If this algorithm hasn't been run before for this day.
        if (userTrendDataRepository.countByObservedDate(datePointer) == 0) {
//            while (datePointer.getTime() < startingEndingDate.getEndingDate().getTime()) {
            LOGGER.info("User Trending Algorithm Started!");

            // Get day + 1
            Calendar c = Calendar.getInstance();
            c.setTime(datePointer);
            c.add(Calendar.DATE, 1);

            // Today's Date
            Date nextDatePointer = c.getTime();

            for (User user : userList) {

                List<UserFollower> userFollowerList = userFollowerRepository.findAllByUserAndCreatedAtBetween(user, datePointer, nextDatePointer);

                int totalFollowers = userFollowerList.size();
                List<PostAction> postActionList = postActionRepository.findAllByUserActorAndCreatedAtBetween(user, datePointer, nextDatePointer);

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

                Long totalPostShared = Long.valueOf(0);
                for (PostAction postAction : postActionList) {
                    totalPostShared += sharePostActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);
                }

                int totalVisits = 0;

                if (totalFollowers <= UserRankDependents.USER_FOLLOWER_COUNT_THRESHOLD
                        && totalPosts <= UserRankDependents.POST_COUNT_THRESHOLD
                        && totalLikes <= UserRankDependents.LIKE_COUNT_THRESHOLD
                        && totalComments <= UserRankDependents.COMMENT_COUNT_THRESHOLD
                        && totalPostVisits <= UserRankDependents.POST_VISIT_COUNT_THRESHOLD
                        && totalPostShared <= UserRankDependents.POST_SHARED_COUNT_THRESHOLD
                        && totalVisits <= UserRankDependents.VISIT_COUNT_THRESHOLD)
                    continue;

                UserRankDTO userRankDTO = new UserRankDTO();
                userRankDTO.setTotalFollowers(Long.valueOf(totalFollowers));
                userRankDTO.setTotalPosts(Long.valueOf(totalPosts));
                userRankDTO.setTotalLikes(totalLikes);
                userRankDTO.setTotalComments(totalComments);
                userRankDTO.setTotalPostVisits(totalPostVisits);
                userRankDTO.setTotalPostShared(totalPostVisits);
                userRankDTO.setTotalVisits(Long.valueOf(0));
                userRankDTO.setRank(this.calculateUserRank(userRankDTO));
                userRankDTO.setDate(datePointer);

                if (userRankDTO.getRank() > UserRankDependents.MIN_THRESHOLD) {
                    UserTrendData userTrendData = new UserTrendData();
                    userTrendData.setUser(user);
                    userTrendData.setObservedDate(datePointer);
                    userTrendData.setRank(userRankDTO.getRank());
                    userTrendDataRepository.save(userTrendData);
                }

            }

            this.calculateUserTrendOverTime();

//                datePointer = nextDatePointer;
            LOGGER.info("User Trending Algorithm Completed! Decided trend xx User");
        } else {
            LOGGER.info("User Trending Algorithm Ignored...");
        }
    }
}
