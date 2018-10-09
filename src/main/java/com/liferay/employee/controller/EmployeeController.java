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

package com.liferay.employee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liferay.employee.model.Employee;
import com.liferay.employee.service.EmployeeService;

/**
 * REST controller to manage employees.
 * 
 * @author Inácio Nery
 */
@RestController
@RequestMapping("/api")
public class EmployeeController {

	private final EmployeeService employeeService;

	private final Logger log =
		LoggerFactory.getLogger(EmployeeController.class);

	public EmployeeController(EmployeeService employeeService) {

		this.employeeService = employeeService;
	}

	/**
	 * POST /v1/employee : Creates a new Employee.
	 * 
	 * <p>
	 * Creates a new Employee if the name is not already used.
	 * </p>
	 *
	 * @param Employee
	 *            the Employee to create
	 * @return the ResponseEntity with status 200 (OK), or with status 400 (Bad
	 *         request) if the name is already in use.
	 */
	@PostMapping("/v1/employee")
	public ResponseEntity<String> createEmployee(
		@RequestBody Employee employee) {

		log.debug("REST request to save Employee : {}", employee);

		if (employeeService.exists(employee)) {
			return ResponseEntity.badRequest().body("Name already in use");
		}

		employeeService.create(employee);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body("New Employee created");
	}

	/**
	 * GET /v1/employee : Get all Employees sorted by name.
	 * 
	 *
	 * @return the ResponseEntity with status 200 (OK).
	 */
	@GetMapping("/v1/employee")
	public ResponseEntity<List<Employee>> getAllEmployeeInOrder() {

		log.debug("REST request all Employees");

		List<Employee> employees = employeeService.findAll();

		return ResponseEntity.ok(employees);
	}
}
