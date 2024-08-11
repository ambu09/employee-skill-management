/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SkillLevel {
  EXPERT, PRACTITIONER, WORKING, AWARENESS;

  /**
   * Get skill-levels.
   *
   */
  public static List<String> getLevels() {
    return Stream.of(SkillLevel.values()).map(skill -> skill.name()).collect(Collectors.toList());
  }
}
