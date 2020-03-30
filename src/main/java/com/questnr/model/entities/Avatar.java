package com.questnr.model.entities;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_avatars")
public class Avatar extends DomainObject{
    @Id
    @Column(name = "avatar_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avatar_seq")
    @SequenceGenerator(name = "avatar_seq", sequenceName = "avatar_seq", allocationSize = 1)
    private Long avatarId;

    @Column(name ="avatar")
    private String avatarKey;

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarKey() {
        return avatarKey;
    }

    public void setAvatarKey(String avatarKey) {
        this.avatarKey = avatarKey;
    }
}
