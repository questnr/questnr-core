package com.questnr.model.entities.activity;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Indexed
@Table(name = "qr_post_activity")
public class PostActivity {
    @Id
    @Column(name = "post_activity_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_activity_seq")
    @SequenceGenerator(name = "post_activity_seq", sequenceName = "post_activity_seq", allocationSize = 1)
    Long postActivityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id", nullable = false)
    private PostAction postAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_actor_id")
    private User userActor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_track", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    private Date lastTrack;

    public PostActivity() {
        this.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastTrack();
    }

    public Long getPostActivityId() {
        return postActivityId;
    }

    public void setPostActivityId(Long postActivityId) {
        this.postActivityId = postActivityId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastTrack() {
        return lastTrack;
    }

    public void setLastTrack() {
        this.lastTrack = Timestamp.valueOf(LocalDateTime.now());
    }
}
