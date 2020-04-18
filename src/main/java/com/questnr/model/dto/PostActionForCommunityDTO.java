package com.questnr.model.dto;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostVisit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PostActionForCommunityDTO {
    private Long postActionId;
    private String slug;
    private String title;
    private String text;
    private PublishStatus status;
    private PostActionPrivacy postActionPrivacy;
    private boolean featured;
    private boolean popular;
    private String tags;
    private UserDTO userDTO;
    private Set<HashTag> hashTags;
    private Set<LikeAction> likeActionSet;
    private List<CommentActionDTO> commentActionDTOList;
    private Set<PostVisit> postVisitSet;
    private List<PostMediaDTO> postMediaDTOList;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
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

    public Set<LikeAction> getLikeActionSet() {
        return likeActionSet;
    }

    public void setLikeActionSet(Set<LikeAction> likeActionSet) {
        this.likeActionSet = likeActionSet;
        try {
            this.setTotalLikes(likeActionSet.size());
        } catch (Exception e) {
            this.setTotalLikes(0);
        }
    }

    public void setCommentActionDTOList(List<CommentActionDTO> commentActionDTOList) {
        this.commentActionDTOList = commentActionDTOList;
        try {
            this.setTotalComments(commentActionDTOList.size());
        } catch (Exception e) {
            this.setTotalComments(0);
        }
    }

    public List<CommentActionDTO> getCommentActionDTOList() {
        if (commentActionDTOList.size() > 0) {
            Predicate<CommentActionDTO> commentActionDTOPredicate = commentActionDTO -> !commentActionDTO.isChildComment();
            commentActionDTOList = commentActionDTOList.stream().filter(commentActionDTOPredicate).collect(Collectors.toList());
            Comparator<CommentActionDTO> createdAtComparator
                    = Comparator.comparing(CommentActionDTO::getCreatedAt);
            commentActionDTOList.sort(createdAtComparator.reversed());
            return commentActionDTOList.subList(0, commentActionDTOList.size() > 3 ? 3 : commentActionDTOList.size());
        }
        return new ArrayList<>();
    }

    public Set<PostVisit> getPostVisitSet() {
        return postVisitSet;
    }


    public void setPostVisitSet(Set<PostVisit> postVisitSet) {
        this.postVisitSet = postVisitSet;
        try {
            this.setTotalPostVisits(postVisitSet.size());
        } catch (Exception e) {
            this.setTotalPostVisits(0);
        }
    }

    public List<PostMediaDTO> getPostMediaDTOList() {
        return postMediaDTOList;
    }

    public void setPostMediaDTOList(List<PostMediaDTO> postMediaDTOList) {
        this.postMediaDTOList = postMediaDTOList;
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
