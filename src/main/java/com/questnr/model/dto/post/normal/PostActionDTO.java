package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.dto.LikeActionDTO;
import com.questnr.model.dto.PostMediaDTO;
import com.questnr.model.dto.post.PostBaseDTO;

import java.util.List;

public class PostActionDTO extends PostBaseDTO {
    private List<LikeActionDTO> likeActionList;
    private List<CommentActionDTO> commentActionList;
    private List<PostMediaDTO> postMediaList;
    private int totalLikes;
    private int totalComments;
    private int totalPostVisits;
    private PostActionMetaDTO postActionMeta;

    public List<LikeActionDTO> getLikeActionList() {
        return likeActionList;
    }

    public void setLikeActionList(List<LikeActionDTO> likeActionList) {
        this.likeActionList = likeActionList;
    }

    public List<CommentActionDTO> getCommentActionList() {
        return commentActionList;
    }

    public void setCommentActionList(List<CommentActionDTO> commentActionList) {
        this.commentActionList = commentActionList;
    }

    public List<PostMediaDTO> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<PostMediaDTO> postMediaList) {
        this.postMediaList = postMediaList;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public int getTotalPostVisits() {
        return totalPostVisits;
    }

    public void setTotalPostVisits(int totalPostVisits) {
        this.totalPostVisits = totalPostVisits;
    }

    public PostActionMetaDTO getPostActionMeta() {
        return postActionMeta;
    }

    public void setPostActionMeta(PostActionMetaDTO postActionMeta) {
        this.postActionMeta = postActionMeta;
    }
}
