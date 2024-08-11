/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.controller;

import com.org.api.employee.skill.model.Employee;
import com.org.api.employee.skill.model.Skill;
import com.org.api.employee.skill.service.EmployeeService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/employee")
public class EmployeeController {

  Logger logger = LoggerFactory.getLogger(EmployeeController.class);

  @Autowired
  private EmployeeService employeeService;

  /**
   * This method is used to get All employee details.
   * 
   * @return employees data
   */
  @GetMapping(value = "/all", produces = "application/json")
  public List<Employee> getAllEmployees() {
    return employeeService.getAllEmployees();
  }

  /**
   * This method is used to get employee details by id.
   * 
   * @param id the employee id.
   * @return employees data
   */
  @GetMapping(value = { "/", "/{id}" }, produces = "application/json")
  public Employee getEmployeeById(@PathVariable Long id) {
    return employeeService.getEmployeeById(id);
  }

  /**
   * This method is used to create new employee.
   * 
   * @param employee the new employee details.
   * @return employee - the newly created employee details
   */
  @PostMapping(value = "/create", produces = "application/json")
  public Employee createEmployee(@Valid @RequestBody Employee employee) {
    return employeeService.createEmployee(employee);
  }

  /**
   * This method is used to update an employee.
   * 
   * @param id              the employee id to be updated.
   * @param updatedEmployee the data to be updated.
   * @return employee - the modified employee details
   */
  @PutMapping(value = { "/", "/{id}" }, produces = "application/json")
  public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
    return employeeService.updateEmployee(id, updatedEmployee);

  }

  /**
   * This method is used to delete an employee.
   * 
   * @param id the employee id to be deleted.
   * @return employee - delete status
   */
  @DeleteMapping("/{id}")
  public String deleteEmployee(@PathVariable Long id) {
    return employeeService.deleteEmployee(id);
  }

  /**
   * This method is used to delete employee skills.
   * 
   * @param skillsToDelete the employee skills to be deleted.
   * @return employee - employee details
   */
  @PutMapping(value = "/{id}/skills", produces = "application/json")
  public Employee deleteEmployeeSkills(@PathVariable Long id, @RequestBody Set<Skill> skillsToDelete) {
    return employeeService.deleteEmployeeSkills(id, skillsToDelete);
  }

  /**
   * This method is used to get employee details by skill name.
   * 
   * @param name the skill name.
   * @return skill and associated employee records
   */
  @GetMapping(value = { "/skill/{name}" }, produces = "application/json")
  public Set<Skill> getEmployeesBySkill(@PathVariable String name) {
    return employeeService.getEmployeesBySkill(name);
  }
}