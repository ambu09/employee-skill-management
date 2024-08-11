/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.exception.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ErrorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String type;
  private String title;
  private int status;

  private String detail;

  private List<ErrorDetailsDto> issues;


  @Getter
  @Setter
  @AllArgsConstructor
  public static class ErrorDetailsDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fieldName;
    private String error;

  }

}
