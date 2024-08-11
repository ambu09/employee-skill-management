/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class EmployeeNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * This method is used to handle CustomerNotFoundException.
   *
   * @param message error message.
   */
  public EmployeeNotFoundException(String message) {
    super(message);

  }
}
