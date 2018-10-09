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

package com.liferay.employee.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.liferay.employee.model.Employee;

/**
 * Repository for the Employee entity.
 * 
 * @author Inácio Nery
 */
@Component
public class EmployeeRepository {

	private List<Employee> employees = new ArrayList<>();

	public boolean exists(Employee employee) {
		return employees.contains(employee);
	}

	public List<Employee> findAll() {
		return employees;
	}

	public void removeAll() {
		employees = new ArrayList<>();
	}

	public Employee save(Employee employee) {
		employees.add(employee);

		return employee;
	}

}
