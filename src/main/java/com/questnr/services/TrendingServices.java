package com.questnr.services;

import com.questnr.services.analytics.CommunityTrendService;
import com.questnr.services.analytics.HashTagTrendService;
import com.questnr.services.analytics.PostActionTrendService;
import com.questnr.services.analytics.UserTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrendingServices {

    @Value("${questnr.allow-trending-services}")
    private boolean allowTrendingServices;

    @Autowired
    CommunityTrendService communityTrendService;

    @Autowired
    PostActionTrendService postActionTrendService;

    @Autowired
    HashTagTrendService hashTagTrendService;

    @Autowired
    UserTrendService userTrendService;

    private Thread communityTrendServiceThread;

    private Thread postActionTrendServiceThread;

    private Thread hashTagTrendServiceThread;

    private Thread userTrendServiceThread;

    private int communityTrendServiceThreadCount = 0;
    private int postActionTrendServiceThreadCount = 0;
    private int hashTagTrendServiceThreadCount = 0;
    private int userTrendServiceThreadCount = 0;


    // @Todo: Set trending services interval time
    @Scheduled(fixedRate = (3600 * 20 * 1000))
    public void scheduledTrendingServices() {
        if (allowTrendingServices) {
            this.startTrendingServices();
        }
    }

    public void startTrendingServices() {
        // Community Trend Service
        this.startCommunityTrendServiceThread();
        // Post Trend Service
        this.startPostActionTrendServiceThread();
        // HashTag Trend Service
        this.startHashTagTrendServiceThread();
        // User Trend Service
        this.startUserTrendServiceThread();
    }

    public void startCommunityTrendServiceThread() {
        if (this.communityTrendServiceThread == null || !this.communityTrendServiceThread.isAlive()) {
            this.communityTrendServiceThread = new Thread(communityTrendService, "communityTrendService-" + this.communityTrendServiceThreadCount++);
            this.communityTrendServiceThread.start();
        }
    }

    public void startPostActionTrendServiceThread() {
        if (this.postActionTrendServiceThread == null || !this.postActionTrendServiceThread.isAlive()) {
            this.postActionTrendServiceThread = new Thread(postActionTrendService, "postActionTrendService-" + this.postActionTrendServiceThreadCount++);
            this.postActionTrendServiceThread.start();
        }
    }

    public void startHashTagTrendServiceThread() {
        if (this.hashTagTrendServiceThread == null || !this.hashTagTrendServiceThread.isAlive()) {
            this.hashTagTrendServiceThread = new Thread(hashTagTrendService, "hashTagTrendService-" + this.hashTagTrendServiceThreadCount++);
            this.hashTagTrendServiceThread.start();
        }
    }

    public void startUserTrendServiceThread() {
        if (this.userTrendServiceThread == null || !this.userTrendServiceThread.isAlive()) {
            this.userTrendServiceThread = new Thread(userTrendService, "userTrendService-" + this.userTrendServiceThreadCount++);
            this.userTrendServiceThread.start();
        }
    }

    public void stopTrendingServices() {
        this.stopCommunityTrendServiceThread();
        this.stopPostActionTrendServiceThread();
        this.stopHashTagTrendServiceThread();
        this.stopUserTrendServiceThread();
    }

    public void stopCommunityTrendServiceThread() {
        if (this.communityTrendServiceThread != null && this.communityTrendServiceThread.isAlive())
            this.communityTrendServiceThread.interrupt();
    }

    public void stopPostActionTrendServiceThread() {
        if (this.postActionTrendServiceThread != null && this.postActionTrendServiceThread.isAlive())
            this.postActionTrendServiceThread.interrupt();
    }

    public void stopHashTagTrendServiceThread() {
        if (this.hashTagTrendServiceThread != null && this.hashTagTrendServiceThread.isAlive())
            this.hashTagTrendServiceThread.interrupt();
    }

    public void stopUserTrendServiceThread() {
        if (this.userTrendServiceThread != null && this.userTrendServiceThread.isAlive())
            this.userTrendServiceThread.interrupt();
    }
}
