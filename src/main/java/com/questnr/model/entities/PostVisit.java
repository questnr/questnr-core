package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_post_visits")
@EntityListeners(AuditingEntityListener.class)
public class PostVisit extends  DomainObject{
    @Id
    @Column(name = "post_visit_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_visit_seq")
    @SequenceGenerator(name = "post_visit_seq", sequenceName = "post_visit_seq", allocationSize = 1)
    Long postVisitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userActor;

//    @OneToOne
//    @JoinColumn(name = "post_view_id")
//    private PostView postView;

    public Long getPostVisitId() {
        return postVisitId;
    }

    public void setPostVisitId(Long postVisitId) {
        this.postVisitId = postVisitId;
    }

    @JsonIgnore
    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    @JsonIgnore
    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

//    public PostView getPostView() {
//        return postView;
//    }
//
//    public void setPostView(PostView postView) {
//        this.postView = postView;
//    }
}
