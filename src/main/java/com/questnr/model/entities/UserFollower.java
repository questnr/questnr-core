package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "qr_user_followers")
@Indexed
public class UserFollower implements NotificationBase {
    @Id
    @Column(name = "user_follower_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_follower_seq")
    @SequenceGenerator(name = "user_follower_seq", sequenceName = "user_follower_seq", allocationSize = 1)
    public Long userFollowerId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "following_user_id")
    private User followingUser;

    @Basic(optional = false)
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = Timestamp.valueOf(LocalDateTime.now());

    public Long getUserFollowerId() {
        return userFollowerId;
    }

    public void setUserFollowerId(Long userFollowerId) {
        this.userFollowerId = userFollowerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User followingUser) {
        this.followingUser = followingUser;
    }

    @JsonIgnore
    public Date getCreatedAt() {
        return createdAt;
    }
}
