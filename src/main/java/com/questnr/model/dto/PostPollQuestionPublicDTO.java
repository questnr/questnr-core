package com.questnr.model.dto;

import com.questnr.model.entities.PostActionMetaInformation;

import java.util.ArrayList;
import java.util.List;

public class PostPollQuestionPublicDTO extends PostBaseDTO {

    private UserOtherDTO userDTO;
    private CommunityForPostActionDTO communityDTO;
    private List<PostActionMetaInformation> metaList = new ArrayList<>();
    private PostPollQuestionDTO postPollQuestion;

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public List<PostActionMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<PostActionMetaInformation> metaList) {
        this.metaList = metaList;
    }

    public PostPollQuestionDTO getPostPollQuestion() {
        return postPollQuestion;
    }

    public void setPostPollQuestion(PostPollQuestionDTO postPollQuestion) {
        this.postPollQuestion = postPollQuestion;
    }
}
