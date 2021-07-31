package com.square.square_project.employee.data

import com.google.common.truth.Truth
import com.square.square_project.EmployeeDataProvider
import com.square.square_project.employee.data.network.FakeEmployeeApi
import com.square.square_project.employee.model.Employees
import com.square.square_project.employee.model.GetEmployeesResponse
import com.square.square_project.utils.RxTestRule
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.adapter.rxjava3.Result
import java.io.IOException

class EmployeeRepositoryTest {
  @Rule
  @JvmField
  val rxTestRule = RxTestRule()

  @Test
  fun `get employees returns 200`() {
    val successResponse = Response.success(EmployeeDataProvider.employees)
    val fakeEmployeeApi = FakeEmployeeApi(Single.just(Result.response(successResponse)))
    val employeeRepository = EmployeeRepositoryImpl(employeeApi = fakeEmployeeApi)

    employeeRepository.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(
        0,
        GetEmployeesResponse.Employees(employees = EmployeeDataProvider.employees.employees)
      )
  }

  @Test
  fun `get employees from api returns 404`() {
    val responseBody = ResponseBody.create(
      MediaType.parse("application/json"), "content")
    val notFoundResponse = Response.error<Employees>(404, responseBody)
    val fakeEmployeeApi = FakeEmployeeApi(Single.just(Result.response(notFoundResponse)))
    val employeeRepository = EmployeeRepositoryImpl(employeeApi = fakeEmployeeApi)

    employeeRepository.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, GetEmployeesResponse.NotFound)
  }

  @Test
  fun `get employees from api returns IOException on network error`() {
    val ioException = IOException("Some random network error")
    val networkErrorResult = Result.error<Employees>(ioException)
    val fakeEmployeeApi = FakeEmployeeApi(Single.just(networkErrorResult))
    val employeeRepository = EmployeeRepositoryImpl(employeeApi = fakeEmployeeApi)

    employeeRepository.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, GetEmployeesResponse.NetworkError)
  }

  @Test
  fun `get employees from api returns 500`() {
    val responseBody = ResponseBody.create(
      MediaType.parse("application/json"), "content")
    val unhandledResponse = Response.error<Employees>(500, responseBody)
    val fakeEmployeeApi = FakeEmployeeApi(Single.just(Result.response(unhandledResponse)))
    val employeeRepository = EmployeeRepositoryImpl(employeeApi = fakeEmployeeApi)

    val values = employeeRepository.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .values()

    Truth.assertThat(values[0]).isInstanceOf(GetEmployeesResponse.UnknownError::class.java)
    Truth.assertThat((values[0] as GetEmployeesResponse.UnknownError).throwable)
      .isInstanceOf(IllegalStateException::class.java)
    Truth.assertThat((values[0] as GetEmployeesResponse.UnknownError).throwable.message)
      .isEqualTo("Unhandled code: 500")
  }

  @Test
  fun `get employees from api returns RuntimeException`() {
    val exception = RuntimeException("Some random logic error")
    val fakeEmployeeApi = FakeEmployeeApi(Single.error(exception))
    val employeeRepository = EmployeeRepositoryImpl(employeeApi = fakeEmployeeApi)

    val values = employeeRepository.getEmployees().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .values()

    Truth.assertThat(values[0]).isInstanceOf(GetEmployeesResponse.UnknownError::class.java)
    Truth.assertThat((values[0] as GetEmployeesResponse.UnknownError).throwable)
      .isInstanceOf(RuntimeException::class.java)
    Truth.assertThat((values[0] as GetEmployeesResponse.UnknownError).throwable.message)
      .isEqualTo("Some random logic error")
  }
}