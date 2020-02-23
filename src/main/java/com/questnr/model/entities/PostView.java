package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_post_views")
@EntityListeners(AuditingEntityListener.class)
public class PostView extends DomainObject {
    @Id
    @Column(name = "post_view_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_view_seq")
    @SequenceGenerator(name = "post_view_seq", sequenceName = "post_view_seq", allocationSize = 1)
    private Long postViewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    public Long getPostViewId() {
        return postViewId;
    }

    public void setPostViewId(Long postViewId) {
        this.postViewId = postViewId;
    }

    @JsonIgnore
    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    @JsonIgnore
    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }
}
