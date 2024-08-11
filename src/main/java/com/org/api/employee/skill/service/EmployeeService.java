/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.service;

import com.org.api.employee.skill.exception.EmployeeNotFoundException;
import com.org.api.employee.skill.exception.InvalidInputException;
import com.org.api.employee.skill.model.Employee;
import com.org.api.employee.skill.model.Skill;
import com.org.api.employee.skill.repository.EmployeeRepository;
import com.org.api.employee.skill.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class EmployeeService {

  Logger logger = LoggerFactory.getLogger(EmployeeService.class);

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private SkillRepository skillRepository;

  /**
   * This method is used to get All employee details.
   * 
   * @return employees data
   */
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  /**
   * This method is used to get employee details by id.
   * 
   * @param id the employee id.
   * @return employees data
   */
  public Employee getEmployeeById(Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (!employee.isPresent()) {
      throw new EmployeeNotFoundException("Employee record not found");
    }
    return employee.get();
  }

  /**
   * This method is used to create new employee.
   * 
   * @param employee the new employee details.
   * @return employee - the newly created employee details
   */
  public Employee createEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  /**
   * This method is used to update an employee.
   * 
   * @param id              the employee id to be updated.
   * @param updatedEmployee the data to be updated.
   * @return employee - the modified employee details
   */
  public Employee updateEmployee(Long id, Employee updatedEmployee) {

    Optional<Employee> employee = employeeRepository.findById(id);

    if (!employee.isPresent()) {
      throw new EmployeeNotFoundException("Employee record not found");
    } else {
      Employee existingEmp = employee.get();
      if (updatedEmployee.getName() != null) {
        existingEmp.setName(updatedEmployee.getName());
      }
      if (updatedEmployee.getEmail() != null) {
        existingEmp.setEmail(updatedEmployee.getEmail());
      }

      // Add or update skills
      if (updatedEmployee.getSkills() != null) {
        for (Skill newSkill : updatedEmployee.getSkills()) {
          validateSkill(newSkill);
          boolean found = false;
          for (Skill existingSkill : existingEmp.getSkills()) {
            if (existingSkill.getName().equalsIgnoreCase(newSkill.getName())) {
              existingSkill.setLevel(newSkill.getLevel());
              found = true;
              break;
            }
          }
          if (!found) {
            existingEmp.getSkills().add(newSkill);
          }
        }
      }
      return employeeRepository.save(existingEmp);

    }

  }

  private void validateSkill(Skill newSkill) {
    if (StringUtils.isBlank(newSkill.getName())) {
      throw new InvalidInputException("skill", "skill.name must not be null/blank", null);
    }
  }

  /**
   * This method is used to delete an employee.
   * 
   * @param id the employee id to be deleted.
   * @return employee - delete status
   */
  public String deleteEmployee(@PathVariable Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (!employee.isPresent()) {
      throw new EmployeeNotFoundException("Employee record not found");
    }
    employeeRepository.delete(employee.get());
    return "Employee record  deleted successfully";

  }

  /**
   * This method is used to delete employee skills.
   * 
   * @param skillsToDelete the employee skills to be deleted.
   * @return employee - employee details
   */
  public Employee deleteEmployeeSkills(Long employeeId, Set<Skill> skillsToDelete) {
    Optional<Employee> employee = employeeRepository.findById(employeeId);

    if (!employee.isPresent()) {
      throw new EmployeeNotFoundException("Employee record not found");
    } else {
      Employee existingEmp = employee.get();

      existingEmp.getSkills().removeIf(
          skill1 -> skillsToDelete.stream().anyMatch(skill2 -> skill2.getName().equalsIgnoreCase(skill1.getName())));
    }
    return employeeRepository.save(employee.get());
  }

  /**
   * This method is used to get employee details by skill name.
   * 
   * @param name the skill name.
   * @return skill and associated employee records
   */
  public Set<Skill> getEmployeesBySkill(String name) {

    return skillRepository.findByNameOrderByLevelAsc(name);

  }
}