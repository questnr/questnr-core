package com.questnr.model.entities;


import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Table(name = "qr_entity_tags")
@Indexed
public class EntityTag extends DomainObject {
    @Id
    @Column(name = "entity_tag_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entity_tag_seq")
    @SequenceGenerator(name = "entity_tag_seq", sequenceName = "entity_tag_seq", allocationSize = 1)
    public Long entityTagId;

    @Column(name = "tag_value", length = 50, unique = true)
    private String tagValue;

    public EntityTag(String tagValue) {
        this.tagValue = tagValue;
        this.addMetadata();
    }

    public Long getEntityTagId() {
        return entityTagId;
    }

    public void setEntityTagId(Long entityTagId) {
        this.entityTagId = entityTagId;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
