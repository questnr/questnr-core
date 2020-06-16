package com.questnr.services.analytics;

import com.questnr.common.PostActionRankDependents;
import com.questnr.common.StartingEndingDate;
import com.questnr.model.dto.PostActionRankDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostActionTrendData;
import com.questnr.model.entities.PostActionTrendLinearData;
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
public class PostActionTrendService implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Autowired
    LikeActionRepository likeActionRepository;

    @Autowired
    PostVisitRepository postVisitRepository;

    @Autowired
    SharePostActionRepository sharePostActionRepository;

    @Autowired
    PostActionTrendDataRepository postActionTrendDataRepository;

    @Autowired
    PostActionTrendLinearDataRepository postActionTrendLinearDataRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    LinearRegressionService linearRegressionService;

    @Value("${questnr.max-trending-posts}")
    private int MAX_TRENDING_POSTS;

    private Double calculatePostActionRank(PostActionRankDTO postActionRankDTO) {
        return PostActionRankDependents.LIKE_ACTION * postActionRankDTO.getTotalLikes() +
                PostActionRankDependents.COMMENT_ACTION * postActionRankDTO.getTotalComments() +
                PostActionRankDependents.POST_VISIT * postActionRankDTO.getTotalPostVisits() +
                PostActionRankDependents.POST_SHARED * postActionRankDTO.getTotalPostShared();
    }

    private List<PostActionTrendLinearData> addPostActionTrendLinearData(List<PostActionTrendLinearData> postActionTrendLinearDataList, PostActionTrendData postActionTrendData) {
        boolean foundInList = false;
        for (PostActionTrendLinearData postActionTrendLinearData : postActionTrendLinearDataList) {
            if (postActionTrendLinearData.getPostAction().equals(postActionTrendData.getPostAction())) {
                postActionTrendLinearData.addX(Double.valueOf(postActionTrendData.getObservedDate().getTime()));
                postActionTrendLinearData.addY(postActionTrendData.getRank());
                foundInList = true;
                break;
            }
        }
        if (!foundInList) {
            PostActionTrendLinearData newPostActionTrendLinearData = new PostActionTrendLinearData();
            newPostActionTrendLinearData.setPostAction(postActionTrendData.getPostAction());
            newPostActionTrendLinearData.addX(Double.valueOf(postActionTrendData.getObservedDate().getTime()));
            newPostActionTrendLinearData.addY(postActionTrendData.getRank());
            postActionTrendLinearDataList.add(newPostActionTrendLinearData);
        }
        return postActionTrendLinearDataList;
    }

    private void calculatePostActionTrendOverTime() {
        // Can be improved this by only finding the slope for those who has new data

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(40);
        startingEndingDate.build();

        List<PostActionTrendData> postActionTrendDataList = postActionTrendDataRepository.findAllByObservedDateBetween(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate());

        List<PostActionTrendLinearData> postActionTrendLinearDataList = new ArrayList<>();
        for (PostActionTrendData postActionTrendData : postActionTrendDataList) {
            addPostActionTrendLinearData(postActionTrendLinearDataList, postActionTrendData);
        }

        for (PostActionTrendLinearData postActionTrendLinearData : postActionTrendLinearDataList) {
            postActionTrendLinearData.setSlop(linearRegressionService.getSlopFromDataOverTime(postActionTrendLinearData.getX(), postActionTrendLinearData.getY()));
        }

        List<PostActionTrendLinearData> pass1PostActionTrendLinearDataList = postActionTrendLinearDataList.stream()
                .filter(postActionTrendLinearData ->
                        !Double.isNaN(postActionTrendLinearData.getSlop())).collect(Collectors.toList());

        // List sorted with descending order of regression slope
        Comparator<PostActionTrendLinearData> postActionTrendLinearDataComparator
                = Comparator.comparing(PostActionTrendLinearData::getSlop);

        pass1PostActionTrendLinearDataList.sort(postActionTrendLinearDataComparator.reversed());

        List<PostActionTrendLinearData> subListPostActionTrendLinearDataList = pass1PostActionTrendLinearDataList
                .subList(0, pass1PostActionTrendLinearDataList.size() > MAX_TRENDING_POSTS ? MAX_TRENDING_POSTS : pass1PostActionTrendLinearDataList.size());

        int trendRank = 1;
        for (PostActionTrendLinearData postActionTrendLinearData : subListPostActionTrendLinearDataList) {
            postActionTrendLinearData.setTrendRank(trendRank++);
        }

        if (subListPostActionTrendLinearDataList.size() > 0) {
            postActionTrendLinearDataRepository.deleteAll();
        }
        postActionTrendLinearDataRepository.saveAll(subListPostActionTrendLinearDataList);
    }

    public void run() {
        this.calculatePostActionTrendOverTime();

        List<PostAction> postActionList = postActionRepository.findAll();


        StartingEndingDate startingEndingDate = new StartingEndingDate();
//        startingEndingDate.setDaysBefore(40);
        startingEndingDate.build();

        // Yesterday's Date
        Date datePointer = startingEndingDate.getStartingDate();

        // If this algorithm hasn't been run before for this day.
        if (postActionTrendDataRepository.countByObservedDate(datePointer) == 0) {
//        while(datePointer.getTime() < startingEndingDate.getEndingDate().getTime()){
            LOGGER.info("Post Action Trending Algorithm Started!");

            // Get day + 1
            Calendar c = Calendar.getInstance();
            c.setTime(datePointer);
            c.add(Calendar.DATE, 1);

            // Today's Date
            Date nextDatePointer = c.getTime();
            Long totalLikes;
            Long totalComments;
            Long totalPostVisits;
            Long totalPostShared;

            for (PostAction postAction : postActionList) {

                totalLikes = likeActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);

                totalComments = commentActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);

                totalPostVisits = postVisitRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);

                totalPostShared = sharePostActionRepository.countAllByPostActionAndCreatedAtBetween(postAction, datePointer, nextDatePointer);

                if (totalLikes <= PostActionRankDependents.LIKE_COUNT_THRESHOLD
                        && totalComments <= PostActionRankDependents.COMMENT_COUNT_THRESHOLD
                        && totalPostVisits <= PostActionRankDependents.POST_VISIT_COUNT_THRESHOLD
                        && totalPostShared <= PostActionRankDependents.POST_SHARED_COUNT_THRESHOLD)
                    continue;

                PostActionRankDTO postActionRankDTO = new PostActionRankDTO();
                postActionRankDTO.setTotalPosts(Long.valueOf(postActionList.size()));
                postActionRankDTO.setTotalLikes(totalLikes);
                postActionRankDTO.setTotalComments(totalComments);
                postActionRankDTO.setTotalPostVisits(totalPostVisits);
                postActionRankDTO.setTotalPostShared(totalPostShared);
                postActionRankDTO.setRank(this.calculatePostActionRank(postActionRankDTO));
                postActionRankDTO.setDate(datePointer);

                if (postActionRankDTO.getRank() > PostActionRankDependents.MIN_THRESHOLD) {
                    PostActionTrendData postActionTrendData = new PostActionTrendData();
                    postActionTrendData.setPostAction(postAction);
                    postActionTrendData.setObservedDate(datePointer);
                    postActionTrendData.setRank(postActionRankDTO.getRank());
                    postActionTrendDataRepository.save(postActionTrendData);
                }

            }

            this.calculatePostActionTrendOverTime();

//            datePointer = nextDatePointer;
            LOGGER.info("Post Action Trending Algorithm Completed!");
        } else {
            LOGGER.info("Post Action Trending Algorithm Ignored...");
        }
    }

}
