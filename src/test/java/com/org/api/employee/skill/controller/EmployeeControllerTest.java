/*
 * Copyright (C) 2024, Oraganization - All Rights Reserved.
 * CONFIDENTIAL INFORMATION - DO NOT DISTRIBUTE
 */

package com.org.api.employee.skill.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.api.employee.skill.exception.EmployeeNotFoundException;
import com.org.api.employee.skill.exception.InvalidInputException;
import com.org.api.employee.skill.model.Employee;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

@SpringJUnitConfig
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  EmployeeController employeeController;

  @Test
  @WithMockUser
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

    given(employeeController.getAllEmployees()).willReturn(empList);

    mockMvc.perform(get("/employee/all")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is(emp1.getName())));

  }

  @Test
  @WithMockUser
  void testGetEmployeeById() throws Exception {

    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");

    given(employeeController.getEmployeeById(1l)).willReturn(emp);

    mockMvc.perform(get("/employee/{id}", 1)).andExpect(status().isOk()).andExpect(jsonPath("name", is(emp.getName())));

  }

  @Test
  @WithMockUser
  void testCreateEmployee() throws Exception {
    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");

    Employee emp1 = new Employee();
    emp1.setName("test");
    emp1.setEmail("test@test.com");

    given(employeeController.createEmployee(emp1)).willReturn(emp);

    mockMvc
        .perform(post("/employee/create").with(csrf()).content(convertIntoJsonString(emp1))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("name", is(emp.getName())));

  }

  @Test
  void testCreateEmployeeInvalid() throws Exception {

    Employee emp1 = new Employee();
    emp1.setEmail("test@test.com");
    InvalidInputException ie = new InvalidInputException("type", "type", null);

    given(employeeController.createEmployee(emp1)).willThrow(ie);

    mockMvc
        .perform(post("/employee/create").content(convertIntoJsonString(emp1)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());

  }

  @Test
  void testGetEmployeeByIdNotFound() throws Exception {

    EmployeeNotFoundException ex = new EmployeeNotFoundException("Not found");

    given(employeeController.getEmployeeById(1l)).willThrow(ex);

    mockMvc.perform(get("/employee/{id}", 1)).andExpect(status().is4xxClientError());

  }

  @Test
  @WithMockUser
  void testUpdateEmployeeById() throws Exception {
    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");

    given(employeeController.updateEmployee(1l, emp)).willReturn(emp);

    mockMvc
        .perform(put("/employee/{id}", 1).with(csrf()).content(convertIntoJsonString(emp))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("name", is(emp.getName())));

  }

  public static String convertIntoJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @WithMockUser
  void testDeleteEmployeeById() throws Exception {
    Employee emp = new Employee();
    emp.setId(1l);
    emp.setName("test");
    emp.setEmail("test@test.com");

    given(employeeController.deleteEmployee(1l)).willReturn("deleted");

    String response = mockMvc.perform(delete("/employee/{id}", 1).with(csrf())).andExpect(status().isOk()).andReturn()
        .getResponse().getContentAsString();
    assertEquals("deleted", response);

  }
}
