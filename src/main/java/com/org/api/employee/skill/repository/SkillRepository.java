/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.repository;

import com.org.api.employee.skill.model.Skill;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

  /*
   * @Query("select e.id, e.name,s.level from Employee e , Skill s where e.id = s.employee_id and s.name=:name"
   * ) Set<Skill> findEmployeeBySkillName(@Param("name") String name);
   */

  Set<Skill> findByNameOrderByLevelAsc(String name);
}