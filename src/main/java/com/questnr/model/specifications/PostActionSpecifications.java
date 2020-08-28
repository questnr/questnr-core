package com.questnr.model.specifications;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.QPostAction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import java.util.List;

public class PostActionSpecifications {

    public static Specification<PostAction> findUsingHashTagList(List<HashTag> tagList) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<PostAction, List<HashTag>> groupJoin = root.join("hashTags");
            final Path<PostAction> tagValue = groupJoin.get("hashTagValue");
            return tagValue.in(tagList);
        };
    }

    public static BooleanExpression hasHashTag(List<HashTag> tags) {
        QPostAction postAction = QPostAction.postAction;
        return postAction.hashTags.any().in(tags);
    }
}
