package com.questnr.controllers;

import com.questnr.services.TrendingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class TrendingServiceController {

    @Autowired
    TrendingServices trendingServices;

    @RequestMapping(value = "/trending-services", method = RequestMethod.POST)
    public void startTrendingServices() {
        trendingServices.startTrendingServices();
    }

    @RequestMapping(value = "/trending-services/community", method = RequestMethod.POST)
    public void startCommunityTrendServiceThread() {
        trendingServices.startCommunityTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services/post", method = RequestMethod.POST)
    public void startPostActionTrendServiceThread() {
        trendingServices.startPostActionTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services/hash-tag", method = RequestMethod.POST)
    public void startHashTagTrendServiceThread() {
        trendingServices.startHashTagTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services/user", method = RequestMethod.POST)
    public void startUserTrendServiceThread() {
        trendingServices.startUserTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services", method = RequestMethod.DELETE)
    public void stopTrendingServices() {
        trendingServices.stopTrendingServices();
    }

    @RequestMapping(value = "/trending-services/community", method = RequestMethod.DELETE)
    public void stopCommunityTrendServiceThread() {
        trendingServices.stopCommunityTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services/post", method = RequestMethod.DELETE)
    public void stopPostActionTrendServiceThread() {
        trendingServices.stopPostActionTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services/hash-tag", method = RequestMethod.DELETE)
    public void stopHashTagTrendServiceThread() {
        trendingServices.stopHashTagTrendServiceThread();
    }

    @RequestMapping(value = "/trending-services/user", method = RequestMethod.DELETE)
    public void stopUserTrendServiceThread() {
        trendingServices.stopUserTrendServiceThread();
    }
}
