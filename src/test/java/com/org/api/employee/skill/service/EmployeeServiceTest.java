/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.org.api.employee.skill.model.Employee;
import com.org.api.employee.skill.repository.EmployeeRepository;
import com.org.api.employee.skill.repository.SkillRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceTest {

  @InjectMocks
  EmployeeService employeeService;

  @Mock
  EmployeeRepository employeeRepository;

  @Mock
  private SkillRepository skillRepository;

  @Test
  void testGetAllEmployees() throws Exception {

    Employee emp1 = new Employee();
    emp1.setId(1l);
    emp1.setName("test");
    emp1.setEmail("test@test.com");

    Employee emp2 = new Employee();
    emp2.setId(2l);
    emp2.setName("bob");
    emp2.setEmail("bob@test.com");

    List<Employee> empList = new ArrayList<>();
    empList.add(emp1);
    empList.add(emp2);

    when(employeeRepository.findAll()).thenReturn(empList);
    List<Employee> result = employeeService.getAllEmployees();

    assertEquals(empList, result);

  }

  @Test
  void testGetEmployeeById() throws Exception {

    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");

    when(employeeRepository.findById(1l)).thenReturn(Optional.of(emp));

    Employee result = employeeService.getEmployeeById(1l);
    assertEquals(emp, result);
  }

  @Test
  void testCreateEmployee() throws Exception {

    Employee emp = new Employee();
    emp.setName("test");
    emp.setEmail("test@test.com");
    when(employeeRepository.save(emp)).thenReturn(emp);
    Employee result = employeeService.createEmployee(emp);
    assertEquals(emp, result);

  }

  @Test
  void testUpdateEmployeeById() throws Exception {
    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");
    when(employeeRepository.findById(1l)).thenReturn(Optional.of(emp));
    when(employeeRepository.save(emp)).thenReturn(emp);
    Employee result = employeeService.updateEmployee(1l, emp);
    assertEquals(emp, result);

  }

  @Test
  void testDeleteEmployeeById() throws Exception {
    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");
    when(employeeRepository.findById(1l)).thenReturn(Optional.of(emp));
    doNothing().when(employeeRepository).delete(emp);

    String result = employeeService.deleteEmployee(1l);
    assertEquals("Employee record  deleted successfully", result);

  }
}