package com.square.square_project.employee.network

import com.square.square_project.employee.model.GetEmployeesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface EmployeeApi {
  @GET("sq-mobile-interview/employees.json")
  fun getEmployees(): Single<Result<GetEmployeesResponse>>

  @GET("sq-mobile-interview/employees_malformed.json")
  fun getEmployeesMalformed(): Single<Result<GetEmployeesResponse>>

  @GET("sq-mobile-interview/employees_empty.json")
  fun getEmployeesEmpty(): Single<Result<GetEmployeesResponse>>
}