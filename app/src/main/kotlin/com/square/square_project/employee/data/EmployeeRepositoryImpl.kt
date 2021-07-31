package com.square.square_project.employee.data

import com.square.square_project.employee.data.network.EmployeeApi
import com.square.square_project.employee.model.GetEmployeesResponse
import io.reactivex.rxjava3.core.Single
import java.io.IOException

class EmployeeRepositoryImpl(val employeeApi: EmployeeApi): EmployeeRepository {
  override fun getEmployees(): Single<GetEmployeesResponse> {
    return employeeApi.getEmployees()
      .map {
        val responseCode = it.response()?.code()
        return@map when {
          responseCode == 200 && it.response()?.body() != null -> {
            GetEmployeesResponse.Employees(
              employees = it.response()!!.body()!!.employees
            )
          }
          it.isError && it.error() == IOException() -> {
            GetEmployeesResponse.NetworkError
          }
          responseCode == 404 -> {
            GetEmployeesResponse.NotFound
          }
          !it.isError && responseCode != null -> {
            GetEmployeesResponse.UnknownError(
              throwable = IllegalStateException("Unhandled code: $responseCode"))
          }
          else -> GetEmployeesResponse.UnknownError(throwable = it.error()!!)
        }
      }.onErrorReturn { throwable ->
        GetEmployeesResponse.UnknownError(throwable = throwable)
      }
  }
}