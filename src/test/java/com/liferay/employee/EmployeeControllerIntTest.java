/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.employee;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.employee.controller.EmployeeController;
import com.liferay.employee.model.Employee;
import com.liferay.employee.service.EmployeeService;

/**
 * Test class for the Employee REST controller.
 * 
 * @author Inácio Nery
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeApplication.class)
public class EmployeeControllerIntTest {

	private final ObjectMapper _mapper = new ObjectMapper();

	@Autowired
	private EmployeeService employeeService;

	private MockMvc restMvc;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		EmployeeController employeeController =
			new EmployeeController(employeeService);

		this.restMvc =
			MockMvcBuilders.standaloneSetup(employeeController).build();
	}

	@After
	public void tearDown() {
		employeeService.removeAll();
	}

	@Test
	public void testGetAllEmployeesSortedByName() throws Exception {
		for (String name : Arrays.asList("Zavala", "Kerry", "Jannat")) {
			Employee employee = new Employee(name);

			restMvc.perform(
				post(
					"/api/v1/employee"
				).contentType(
					MediaType.APPLICATION_JSON_UTF8_VALUE
				).content(
					_mapper.writeValueAsBytes(employee)
				)
			).andExpect(
				status().isCreated()
			);
		}

		restMvc.perform(get("/api/v1/employee")).andExpect(
			status().isOk()
		).andExpect(
			jsonPath("$.[0].name").value(is("Jannat"))
		).andExpect(
			jsonPath("$.[1].name").value(is("Kerry"))
		).andExpect(
			jsonPath("$.[2].name").value(is("Zavala"))
		);
	}

	@Test
	public void testRegisterDuplicateEmployee() throws Exception {
		Employee employee = new Employee("Metcalfe");

		restMvc.perform(
			post(
				"/api/v1/employee"
			).contentType(
				MediaType.APPLICATION_JSON_UTF8_VALUE
			).content(
				_mapper.writeValueAsBytes(employee)
			)
		).andExpect(
			status().isCreated()
		);
		
		restMvc.perform(
			post(
				"/api/v1/employee"
			).contentType(
				MediaType.APPLICATION_JSON_UTF8_VALUE
			).content(
				_mapper.writeValueAsBytes(employee)
			)
		).andExpect(
			status().isBadRequest()
		);
	}
}
