package com.square.square_project.employee.data

import com.square.square_project.employee.model.GetEmployeesResponse
import io.reactivex.rxjava3.core.Single

class EmployeeUseCaseImpl(val employeeRepository: EmployeeRepository): EmployeeUseCase {
  override fun getEmployees(): Single<GetEmployeesResponse> {
    return employeeRepository.getEmployees()
  }
}