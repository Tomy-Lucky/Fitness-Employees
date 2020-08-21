package com.arcadegamepark.fitnessemployees.screens.employees;

import android.widget.Toast;

import com.arcadegamepark.fitnessemployees.api.ApiFactory;
import com.arcadegamepark.fitnessemployees.api.ApiService;
import com.arcadegamepark.fitnessemployees.pojo.EmployeeResponce;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EmployeeListPresenter {

    private CompositeDisposable compositeDisposable; // Like ArrayList for all Disposable Objects
    private Disposable disposable; // One time Object

    private EmployeeListView employeeListView;

    public EmployeeListPresenter(EmployeeListView employeeListView) {
        this.employeeListView = employeeListView;
    }

    public void loadData() {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        disposable = apiService.getEmployees() // Disposable is one time Object
                .subscribeOn(Schedulers.io()) // To do in another Thread
                .observeOn(AndroidSchedulers.mainThread()) // Get data in main Thread
                .subscribe(new Consumer<EmployeeResponce>() {
                    @Override
                    public void accept(EmployeeResponce employeeResponce) throws Exception { // Success case
                        employeeListView.showData(employeeResponce.getEmployees());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception { // Exception Case
                        employeeListView.showError(throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable); // We add all Disposable objects to CompositeDisposable(it is like ArrayList for this type of object)
    }

    public void disposeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
