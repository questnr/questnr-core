package com.questnr.model.specifications;


import com.questnr.common.enums.SearchCriteria;
import com.questnr.model.entities.PostAction;
import com.questnr.requests.Filter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrendingPostSpecificationBuilder {

  private final List<SearchCriteria> params;

  public TrendingPostSpecificationBuilder() {
    params = new ArrayList<SearchCriteria>();
  }

  public TrendingPostSpecificationBuilder with(String key,String operation, String value) {
    params.add(new SearchCriteria(key, operation, value));
    return this;
  }

  public Specification<PostAction> build(LinkedList<SearchCriteria> params) {
    if (params.size() == 0) {
      return null;
    }

    LinkedList<Specification<PostAction>> specs = new LinkedList<>();

    for (SearchCriteria param : params) {
      specs.add(new TrendingPostSpecification(param));
    }

    Specification<PostAction> result = specs.get(0);

    for (int i = 1; i < specs.size(); i++) {
      result = result.and(specs.get(i));
    }

    return result;
  }
}
