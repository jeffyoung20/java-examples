package com.jeffy.multipledbs.classicmodels.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffy.multipledbs.classicmodels.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
}
