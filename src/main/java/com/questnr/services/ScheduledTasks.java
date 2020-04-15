package com.questnr.services;

import com.questnr.services.community.CommunityTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    CommunityTrendService communityTrendService;

    @Autowired
    PostActionTrendService postActionTrendService;

    @Scheduled(fixedRate = (3600 * 24 * 1000))
    public void storeTrendData() {

        // Community Trend Service
        communityTrendService.run();

        // Post Trend Service
        postActionTrendService.run();
    }
}
