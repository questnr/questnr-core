package com.questnr.model.entities.activity;

import com.questnr.model.entities.Community;
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
@Table(name = "qr_community_activity")
public class CommunityActivity {
    @Id
    @Column(name = "community_activity_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_activity_seq")
    @SequenceGenerator(name = "community_activity_seq", sequenceName = "community_activity_seq", allocationSize = 1)
    Long communityActivityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

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

    public CommunityActivity() {
        this.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastTrack();
    }

    public Long getCommunityActivityId() {
        return communityActivityId;
    }

    public void setCommunityActivityId(Long communityActivityId) {
        this.communityActivityId = communityActivityId;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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
