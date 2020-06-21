package com.questnr.model.dto;

import com.questnr.common.enums.PostActionType;

public class PostPollQuestionFeedDTO extends PostBaseDTO {

    private CommunityForPostActionDTO communityDTO;
    private PostActionType postActionType;
    private UserOtherDTO userDTO;
    private PostPollQuestionDTO postPollQuestion;

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public PostActionType getPostActionType() {
        return postActionType;
    }

    public void setPostActionType(PostActionType postActionType) {
        this.postActionType = postActionType;
    }

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }

    public PostPollQuestionDTO getPostPollQuestion() {
        return postPollQuestion;
    }

    public void setPostPollQuestion(PostPollQuestionDTO postPollQuestion) {
        this.postPollQuestion = postPollQuestion;
    }
}
