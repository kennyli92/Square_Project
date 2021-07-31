package com.square.square_project.employee.data.network

import com.square.square_project.employee.model.Employees
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.Result

class FakeEmployeeApi(
  private val employeesResponse: Single<Result<Employees>>
) : EmployeeApi {
  override fun getEmployees(): Single<Result<Employees>> {
    return employeesResponse
  }

  override fun getEmployeesEmpty(): Single<Result<Employees>> {
    TODO("Not yet implemented")
  }

  override fun getEmployeesMalformed(): Single<Result<Employees>> {
    TODO("Not yet implemented")
  }
}