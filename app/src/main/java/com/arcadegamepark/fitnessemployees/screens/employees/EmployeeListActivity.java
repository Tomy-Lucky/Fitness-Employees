package com.arcadegamepark.fitnessemployees.screens.employees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arcadegamepark.fitnessemployees.adapters.EmployeeAdapter;
import com.arcadegamepark.fitnessemployees.R;
import com.arcadegamepark.fitnessemployees.pojo.Employee;
import com.arcadegamepark.fitnessemployees.pojo.Speciality;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListActivity extends AppCompatActivity { //  implements EmployeeListView (Presenter Code)

    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;

    // Presenter Code
//    private EmployeeListPresenter presenter;

    private EmployeeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        employeeAdapter = new EmployeeAdapter();
        employeeAdapter.setEmployees(new ArrayList<Employee>());
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmployees.setAdapter(employeeAdapter);

        viewModel = ViewModelProviders.of(this).get(EmployeeViewModel.class);
        viewModel.getEmployees().observe(this, new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employees) {
                employeeAdapter.setEmployees(employees);
                if (employees != null) {
                    for (Employee employee: employees) {
                        List<Speciality> specialities = employee.getSpeciality();
                        for (Speciality speciality: specialities) {
                            Log.i("Speciality", speciality.getName());
                        }
                    }
                }
            }
        });
        viewModel.getErrors().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null) {
                    Toast.makeText(EmployeeListActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewModel.clearErrors();
                }
            }
        });
        viewModel.loadData();

        // Presenter Code
//        presenter = new EmployeeListPresenter(this);
//        presenter.loadData();

    }

    // Presenter Code
//    @Override
//    protected void onDestroy() {
//        presenter.disposeDisposable();
//        super.onDestroy();
//    }
//
//    @Override
//    public void showData(List<Employee> employees) {
//        employeeAdapter.setEmployees(employees);
//    }
//
//    @Override
//    public void showError(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
//    }

}