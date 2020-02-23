package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_hash_tags")
@EntityListeners(AuditingEntityListener.class)
public class HashTag extends DomainObject {
    @Id
    @Column(name = "hash_tag_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hash_tag_seq")
    @SequenceGenerator(name = "hash_tag_seq", sequenceName = "hash_tag_seq", allocationSize = 1)
    private Long hashTagId;

    @Column(name = "hash_tag_value")
    private String hashTagValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userCreator;

//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            },
//            mappedBy = "tags")
//    private Set<PostAction> posts = new HashSet<>();

    public Long getHashTagId() {
        return hashTagId;
    }

    public void setHashTagId(Long hashTagId) {
        this.hashTagId = hashTagId;
    }

    public String getHashTagValue() {
        return hashTagValue;
    }

    public void setHashTagValue(String hashTagValue) {
        this.hashTagValue = hashTagValue;
    }

    @JsonIgnore
    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }

//    public Set<PostAction> getPosts() {
//        return posts;
//    }
//
//    public void setPosts(Set<PostAction> posts) {
//        this.posts = posts;
//    }
}
