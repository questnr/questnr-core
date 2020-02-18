package com.questnr.model.specifications;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.PostAction;
import com.questnr.requests.Filter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PostActionSpecification implements Specification<PostAction> {

  private Filter criteria;

  public PostActionSpecification(Filter criteria) {
    super();
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<PostAction> root, CriteriaQuery<?> criteriaQuery,
      CriteriaBuilder criteriaBuilder) {

    if(root.get(criteria.getFilterName()).getJavaType() == String.class){
      return criteriaBuilder.like(root.get(criteria.getFilterName()), "%" + criteria.getValue() + "%");
    }
    else if (root.get(criteria.getFilterName()).getJavaType() == PublishStatus.class) {
      return criteriaBuilder.equal(root.get(criteria.getFilterName()),
          PublishStatus.valueOf(criteria.getValue()));
    }
    else {
      return criteriaBuilder.equal(root.get(criteria.getFilterName()), criteria.getValue());
    }

  }
}
