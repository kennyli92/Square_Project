package com.square.square_project.employee.data

import com.square.square_project.employee.model.GetEmployeesResponse
import io.reactivex.rxjava3.core.Single

class FakeEmployeeRepository(
  private val getEmployeesResponse: Single<GetEmployeesResponse>
) : EmployeeRepository {
  override fun getEmployees(): Single<GetEmployeesResponse> {
    return getEmployeesResponse
  }
}