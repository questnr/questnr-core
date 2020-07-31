package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.dto.LikeActionDTO;
import com.questnr.model.dto.MediaDTO;
import com.questnr.model.dto.post.PostBaseDTO;

import java.util.List;

public class PostActionDTO extends PostBaseDTO {
    private NormalPostDTO postData;
    private List<LikeActionDTO> likeActionList;
    private List<CommentActionDTO> commentActionList;
    private List<MediaDTO> postMediaList;
    private PostActionMetaDTO postActionMeta;

    public NormalPostDTO getPostData() {
        return postData;
    }

    public void setPostData(NormalPostDTO postData) {
        this.postData = postData;
    }

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

    public List<MediaDTO> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<MediaDTO> postMediaList) {
        this.postMediaList = postMediaList;
    }

    public PostActionMetaDTO getPostActionMeta() {
        return postActionMeta;
    }

    public void setPostActionMeta(PostActionMetaDTO postActionMeta) {
        this.postActionMeta = postActionMeta;
    }
}
