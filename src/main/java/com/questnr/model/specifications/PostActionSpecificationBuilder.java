package com.questnr.model.specifications;


import com.questnr.model.entities.PostAction;
import com.questnr.requests.Filter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PostActionSpecificationBuilder {

  private final List<Filter> params;

  public PostActionSpecificationBuilder() {
    params = new ArrayList<Filter>();
  }

  public PostActionSpecificationBuilder with(String key, String value) {
    params.add(new Filter(key, value));
    return this;
  }

  public Specification<PostAction> build(LinkedList<Filter> params) {
    if (params.size() == 0) {
      return null;
    }

    LinkedList<Specification<PostAction>> specs = new LinkedList<>();

    for (Filter param : params) {
      specs.add(new PostActionSpecification(param));
    }

    Specification<PostAction> result = specs.get(0);

    for (int i = 1; i < specs.size(); i++) {
      result = result.and(specs.get(i));
    }

    return result;
  }
}
