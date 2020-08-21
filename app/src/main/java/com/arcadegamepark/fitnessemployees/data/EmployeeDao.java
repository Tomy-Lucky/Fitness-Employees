package com.arcadegamepark.fitnessemployees.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arcadegamepark.fitnessemployees.pojo.Employee;

import java.util.List;

@Dao
public interface EmployeeDao {

    @Query("SELECT * FROM employees")
    LiveData<List<Employee>> getAllEmployees();

    @Insert(onConflict = OnConflictStrategy.REPLACE) // We just replace element if there is such element in Database
    void insertEmployee(List<Employee> employees);

    @Query("DELETE FROM employees")
    void deleteAllEmployees();
}
