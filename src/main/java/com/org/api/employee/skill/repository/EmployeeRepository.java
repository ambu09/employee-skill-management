/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.repository;

import com.org.api.employee.skill.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}