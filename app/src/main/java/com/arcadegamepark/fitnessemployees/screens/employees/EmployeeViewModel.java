package com.arcadegamepark.fitnessemployees.screens.employees;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.arcadegamepark.fitnessemployees.api.ApiFactory;
import com.arcadegamepark.fitnessemployees.api.ApiService;
import com.arcadegamepark.fitnessemployees.data.AppDatabase;
import com.arcadegamepark.fitnessemployees.pojo.Employee;
import com.arcadegamepark.fitnessemployees.pojo.EmployeeResponce;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EmployeeViewModel extends AndroidViewModel {

    private static AppDatabase db;
    private LiveData<List<Employee>> employees;
    private MutableLiveData<Throwable> errors;
    private CompositeDisposable compositeDisposable; // Like ArrayList for all Disposable Objects

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        employees = db.employeeDao().getAllEmployees();
        errors = new MutableLiveData<>();
    }

    public LiveData<List<Employee>> getEmployees() {
        return employees;
    }

    public LiveData<Throwable> getErrors() { // We changed method to LiveData, because we should not change the Data
        return errors;
    }

    public void clearErrors() {
        errors.setValue(null);
    }

    @SuppressWarnings("unchecked") // We block Warnings, which come when we fully fill memory
    public void insertEmployees(List<Employee> employees) {
        new insertEmployeesTask().execute(employees);
    }

    private static class insertEmployeesTask extends AsyncTask<List<Employee>, Void, Void> {
        @SafeVarargs // Also we block warnings about filling memory
        @Override
        protected final Void doInBackground(List<Employee>... lists) {
            if (lists != null && lists.length > 0) {
                db.employeeDao().insertEmployee(lists[0]);
            }
            return null;
        }
    }

    private void deleteAllEmployees() {
        new deleteAllEmployeesTask().execute();
    }

    private static class deleteAllEmployeesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.employeeDao().deleteAllEmployees();
            return null;
        }
    }

    public void loadData() {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();

        compositeDisposable = new CompositeDisposable();
        Disposable disposable = apiService.getEmployees() // Disposable is one time Object
                .subscribeOn(Schedulers.io()) // To do in another Thread
                .observeOn(AndroidSchedulers.mainThread()) // Get data in main Thread
                .subscribe(new Consumer<EmployeeResponce>() {
                    @Override
                    public void accept(EmployeeResponce employeeResponce) throws Exception { // Success case
                        deleteAllEmployees();
                        insertEmployees(employeeResponce.getEmployees());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception { // Exception Case
                        errors.setValue(throwable);
                    }
                });
        compositeDisposable.add(disposable); // We add all Disposable objects to CompositeDisposable(it is like ArrayList for this type of object)
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
