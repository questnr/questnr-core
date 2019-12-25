package com.totality.responses;

import com.totality.model.entities.LikeAction;

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
