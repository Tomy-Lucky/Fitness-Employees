package com.arcadegamepark.fitnessemployees.screens.employees;

import com.arcadegamepark.fitnessemployees.pojo.Employee;

import java.util.List;

public interface EmployeeListView {

    void showData(List<Employee> employees);
    void showError(String errorMessage);
}
