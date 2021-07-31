package com.square.square_project.employee.data.network

import com.square.square_project.employee.model.Employee
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.Result
import retrofit2.http.GET

interface EmployeeApi {
  @GET("sq-mobile-interview/employees.json")
  fun getEmployees(): Single<Result<List<Employee>>>

  @GET("sq-mobile-interview/employees_malformed.json")
  fun getEmployeesMalformed(): Single<Result<List<Employee>>>

  @GET("sq-mobile-interview/employees_empty.json")
  fun getEmployeesEmpty(): Single<Result<List<Employee>>>
}