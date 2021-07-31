package com.square.square_project.employee.data

import com.square.square_project.EmployeeDataProvider
import com.square.square_project.employee.model.GetEmployeesResponse
import com.square.square_project.utils.RxTestRule
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test

class EmployeeUseCaseTest {
  @Rule
  @JvmField
  val rxTestRule = RxTestRule()

  @Test
  fun `get employees returns Employees response when not empty`() {
    val fakeEmployeeRepository = FakeEmployeeRepository(
      getEmployeesResponse = Single.just(GetEmployeesResponse.Employees(EmployeeDataProvider.employees.employees))
    )
    val employeeUseCase = EmployeeUseCaseImpl(employeeRepository = fakeEmployeeRepository)

    employeeUseCase.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, GetEmployeesResponse.Employees(EmployeeDataProvider.employees.employees))
  }

  @Test
  fun `get employees returns Not Found response when empty`() {
    val fakeEmployeeRepository = FakeEmployeeRepository(
      getEmployeesResponse = Single.just(GetEmployeesResponse.Employees(emptyList()))
    )
    val employeeUseCase = EmployeeUseCaseImpl(employeeRepository = fakeEmployeeRepository)

    employeeUseCase.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, GetEmployeesResponse.NotFound)
  }
}