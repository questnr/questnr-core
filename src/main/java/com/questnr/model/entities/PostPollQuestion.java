package com.questnr.model.entities;

import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
@Table(name = "qr_post_poll_questions")
@Where(clause = "deleted = false")
public class PostPollQuestion {
    @Id
    @Column(name = "post_poll_question_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_poll_question_seq")
    @SequenceGenerator(name = "post_poll_question_seq", sequenceName = "post_poll_question_seq", allocationSize = 1)
    private Long pollQuestionActionId;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    private boolean deleted;

    @Column(name = "agree_text", nullable = false)
    private String agreeText;

    @Column(name = "disagree_text", nullable = false)
    private String disagreeText;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_poll_question_id", nullable = false)
    private List<PostPollAnswer> postPollAnswer = new ArrayList<>();

    public Long getPollQuestionActionId() {
        return pollQuestionActionId;
    }

    public void setPollQuestionActionId(Long pollQuestionActionId) {
        this.pollQuestionActionId = pollQuestionActionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAgreeText() {
        return agreeText;
    }

    public void setAgreeText(String agreeText) {
        this.agreeText = agreeText;
    }

    public String getDisagreeText() {
        return disagreeText;
    }

    public void setDisagreeText(String disagreeText) {
        this.disagreeText = disagreeText;
    }

    public List<PostPollAnswer> getPostPollAnswer() {
        return postPollAnswer;
    }

    public void setPostPollAnswer(List<PostPollAnswer> postPollAnswer) {
        this.postPollAnswer = postPollAnswer;
    }
}
