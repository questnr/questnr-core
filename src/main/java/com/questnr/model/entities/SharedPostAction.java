package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Indexed
@Table(name = "qr_shared_post_actions")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause = "deleted = false")
public class SharedPostAction extends DomainObject {
    @Id
    @Column(name = "shared_post_action_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shared_post_action_seq")
    @SequenceGenerator(name = "shared_post_action_seq", sequenceName = "shared_post_action_seq", allocationSize = 1)
    private Long sharedPostActionId;

    @JsonIgnoreProperties("sharedPostActionSet")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id")
    private PostAction postAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_actor_id")
    private User userActor;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    private boolean deleted;

    public Long getSharedPostActionId() {
        return sharedPostActionId;
    }

    public void setSharedPostActionId(Long sharedPostActionId) {
        this.sharedPostActionId = sharedPostActionId;
    }

    public PostAction getPostAction() {
        return postAction;
    }

    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    public User getUserActor() {
        return userActor;
    }

    public void setUserActor(User userActor) {
        this.userActor = userActor;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedPostAction that = (SharedPostAction) o;
        return postAction.equals(that.postAction) &&
                userActor.equals(that.userActor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postAction, userActor);
    }
}
