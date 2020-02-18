package com.questnr.requests;

import com.questnr.common.enums.PublishStatus;

public class Filter {

  String filterName;
  String value;

  public Filter() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Filter(String filterName, String value) {
    super();
    this.filterName = filterName;
    this.value = value;
  }

  public static Filter addDefaultFilter(){
    Filter filter = new Filter();
    filter.setFilterName("status");
    filter.setValue(PublishStatus.publish.jsonValue);
    return filter;
  }


  /**
   * @return the filterName
   */
  public String getFilterName() {
    return filterName;
  }

  /**
   * @param filterName the filterName to set
   */
  public void setFilterName(String filterName) {
    this.filterName = filterName;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }
}
