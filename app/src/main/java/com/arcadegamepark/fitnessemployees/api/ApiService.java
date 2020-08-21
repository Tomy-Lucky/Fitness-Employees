package com.arcadegamepark.fitnessemployees.api;

import com.arcadegamepark.fitnessemployees.pojo.EmployeeResponce;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    @GET("testTask.json")
        // We set endPoint(the last part of URL)
    Observable<EmployeeResponce> getEmployees();
}
