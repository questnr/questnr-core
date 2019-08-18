package com.totality.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

  String resourceName;
  String fieldName;
  String fieldValue;

  public InvalidInputException(String resourceName, String fieldName, String fieldValue) {
    super();
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  /**
   * @return the resourceName
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * @param resourceName the resourceName to set
   */
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  /**
   * @return the fieldName
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * @param fieldName the fieldName to set
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * @return the fieldValue
   */
  public String getFieldValue() {
    return fieldValue;
  }

  /**
   * @param fieldValue the fieldValue to set
   */
  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }
}
