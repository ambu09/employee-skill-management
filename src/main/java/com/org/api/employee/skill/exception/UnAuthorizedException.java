/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnAuthorizedException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  public UnAuthorizedException(String message) {
    super(message);
  }
    
}
