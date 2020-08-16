package com.questnr.model.entities.activity;

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
@Table(name = "qr_user_activity")
public class UserActivity {
    @Id
    @Column(name = "user_activity_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_activity_seq")
    @SequenceGenerator(name = "user_activity_seq", sequenceName = "user_activity_seq", allocationSize = 1)
    Long userActivityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Column(name = "referrer")
    private String referrer;

    public UserActivity() {
        this.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastTrack();
    }

    public Long getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLastTrack(Date lastTrack) {
        this.lastTrack = lastTrack;
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

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
