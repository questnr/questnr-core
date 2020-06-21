package com.questnr.model.dto;

public class PostPollQuestionForCommunityDTO extends PostBaseDTO {

    private UserOtherDTO userDTO;
    private PostPollQuestionDTO postPollQuestion;

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
