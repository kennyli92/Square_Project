package com.square.square_project.employee.data

import com.square.square_project.employee.model.GetEmployeesResponse
import io.reactivex.rxjava3.core.Single

interface EmployeeUseCase {
  fun getEmployees(): Single<GetEmployeesResponse>
}