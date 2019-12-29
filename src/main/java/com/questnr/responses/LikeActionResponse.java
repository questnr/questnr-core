package com.questnr.responses;

import com.questnr.model.entities.LikeAction;

import java.util.List;

public class LikeActionResponse {
    List<LikeAction> likeList;

    public LikeActionResponse() {

    }

    public LikeActionResponse(List<LikeAction> likeList) {
        this.likeList = likeList;
    }

    public List<LikeAction> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeAction> likeList) {
        this.likeList = likeList;
    }
}
