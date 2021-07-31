package com.square.square_project.employee.model

sealed class GetEmployeesResponse {
  data class Employees(
    val employees: List<Employee>
  ) : GetEmployeesResponse()

  object NotFound : GetEmployeesResponse()

  object NetworkError : GetEmployeesResponse()

  data class UnknownError(
    val throwable: Throwable
  ) : GetEmployeesResponse()
}