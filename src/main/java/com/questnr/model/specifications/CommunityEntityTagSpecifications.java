package com.questnr.model.specifications;

import com.questnr.model.entities.CommunityTag;
import com.questnr.model.entities.EntityTag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import java.util.List;

public class CommunityEntityTagSpecifications {

    public static Specification<CommunityTag> findEntityTagInList(List<String> tagList) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<CommunityTag, EntityTag> groupJoin = root.join("entityTag");
            final Path<CommunityTag> tagValue = groupJoin.get("tagValue");
            return tagValue.in(tagList);
        };
    }
}
