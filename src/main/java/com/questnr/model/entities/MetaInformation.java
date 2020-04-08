package com.questnr.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MetaInformation {

  /**
   * Meta type
   */
  @Column(name = "meta_type")
  private String type;

  /**
   * Comma separated description of a type
   */
  @Column(name = "meta_content", columnDefinition = "TEXT")
  private String content;

  @Column(name = "meta_attribute_type")
  private String attributeType;

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the attributeType
   */
  public String getAttributeType() {
    return attributeType;
  }

  /**
   * @param attributeType the attributeType to set
   */
  public void setAttributeType(String attributeType) {
    this.attributeType = attributeType;
  }
}
