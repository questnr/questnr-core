package com.questnr.model.entities;

import com.questnr.common.enums.PostPollAnswerType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_post_poll_answers")
public class PostPollAnswer {
    @Id
    @Column(name = "post_poll_answer_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_poll_answer_seq")
    @SequenceGenerator(name = "post_poll_answer_seq", sequenceName = "post_poll_answer_seq", allocationSize = 1)
    private Long pollAnswerId;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "answer", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostPollAnswerType answer;

    public Long getPollAnswerId() {
        return pollAnswerId;
    }

    public void setPollAnswerId(Long pollAnswerId) {
        this.pollAnswerId = pollAnswerId;
    }

    public PostPollAnswerType getAnswer() {
        return answer;
    }

    public void setAnswer(PostPollAnswerType answer) {
        this.answer = answer;
    }
}
