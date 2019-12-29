package com.questnr.requests;

public class LikeActionRequest {
    Long likeId;
    Long postId;

    public LikeActionRequest(Long likeId, Long postId) {
        this.likeId = likeId;
        this.postId = postId;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
