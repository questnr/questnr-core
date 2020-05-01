package com.questnr.model.dto;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.model.entities.HashTag;

import java.util.List;
import java.util.Set;

public class PostActionForCommunityDTO {
    private Long postActionId;
    private String slug;
    private String text;
    private PostActionPrivacy postActionPrivacy;
    private boolean featured;
    private boolean popular;
    private String tags;
    private UserDTO userDTO;
    private Set<HashTag> hashTags;
    private List<LikeActionDTO> likeActionList;
    private List<CommentActionDTO> commentActionList;
    private List<PostMediaDTO> postMediaList;
    private int totalLikes;
    private int totalComments;
    private int totalPostVisits;
    private MetaDataDTO metaData;

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PostActionPrivacy getPostActionPrivacy() {
        return postActionPrivacy;
    }

    public void setPostActionPrivacy(PostActionPrivacy postActionPrivacy) {
        this.postActionPrivacy = postActionPrivacy;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Set<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(Set<HashTag> hashTags) {
        this.hashTags = hashTags;
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

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }
}
