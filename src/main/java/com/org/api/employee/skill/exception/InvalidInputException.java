/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class InvalidInputException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String fieldName;

  public InvalidInputException(String fieldName, String message, Throwable cause) {
    super(message, cause);
    this.fieldName = fieldName;
  }
}
