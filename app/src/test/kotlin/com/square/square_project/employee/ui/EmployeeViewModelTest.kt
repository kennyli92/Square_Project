package com.square.square_project.employee.ui

import com.google.android.material.snackbar.Snackbar
import com.google.common.truth.Truth
import com.square.square_project.EmployeeDataProvider
import com.square.square_project.R
import com.square.square_project.employee.data.FakeEmployeeUseCase
import com.square.square_project.employee.model.GetEmployeesResponse
import com.square.square_project.utils.RxTestRule
import com.square.square_project.utils.SnackbarModel
import com.square.square_project.utils.Visibility
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test

class EmployeeViewModelTest {
  @Rule
  @JvmField
  val rxTestRule = RxTestRule()

  @Test
  fun `on load action calls onGetEmployees on first load`() {
    // GIVEN
    val getEmployeesResponse = Single.just(
      GetEmployeesResponse.Employees(EmployeeDataProvider.employeeList) as GetEmployeesResponse)
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = getEmployeesResponse)
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = employeeViewModel.stateObs().test()
    val eventObs = employeeViewModel.eventObs().test()
    actionSignal.onNext(EmployeeAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeState.ProgressVisibility(visibility = Visibility.VISIBLE))
      .assertValueAt(1, EmployeeState.ListItem(
        employeeItems = EmployeeDataProvider.employeeItemsSortedByFullname
      ))
      .assertValueAt(2, EmployeeState.ProgressVisibility(visibility = Visibility.GONE))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeEvent.Noop)
      .assertValueAt(1, EmployeeEvent.Noop)
      .assertValueAt(2, EmployeeEvent.Noop)
  }

  @Test
  fun `on load action returns employee items if we already have employees loaded`() {
    // GIVEN
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = Single.error(IllegalStateException("Network call not made!")))
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)
    employeeViewModel.employees = EmployeeDataProvider.employeeList

    // WHEN
    val stateObs = employeeViewModel.stateObs().test()
    val eventObs = employeeViewModel.eventObs().test()
    actionSignal.onNext(EmployeeAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, EmployeeState.ListItem(
        employeeItems = EmployeeDataProvider.employeeItemsSortedByFullname
      ))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, EmployeeEvent.Noop)
  }

  @Test
  fun `on Get Employees action returns employee items`() {
    // GIVEN
    val getEmployeesResponse = Single.just(
      GetEmployeesResponse.Employees(EmployeeDataProvider.employeeList) as GetEmployeesResponse)
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = getEmployeesResponse)
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = employeeViewModel.stateObs().test()
    val eventObs = employeeViewModel.eventObs().test()
    actionSignal.onNext(EmployeeAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeState.ProgressVisibility(visibility = Visibility.VISIBLE))
      .assertValueAt(1, EmployeeState.ListItem(
        employeeItems = EmployeeDataProvider.employeeItemsSortedByFullname
      ))
      .assertValueAt(2, EmployeeState.ProgressVisibility(visibility = Visibility.GONE))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeEvent.Noop)
      .assertValueAt(1, EmployeeEvent.Noop)
      .assertValueAt(2, EmployeeEvent.Noop)
  }

  @Test
  fun `on Get Employees action when network error`() {
    // GIVEN
    val getEmployeesResponse = Single.just(GetEmployeesResponse.NetworkError as GetEmployeesResponse)
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = getEmployeesResponse)
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = employeeViewModel.stateObs().test()
    val eventObs = employeeViewModel.eventObs().test()
    actionSignal.onNext(EmployeeAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeState.ProgressVisibility(visibility = Visibility.VISIBLE))
      .assertValueAt(1, EmployeeState.EmptyState)
      .assertValueAt(2, EmployeeState.ProgressVisibility(visibility = Visibility.GONE))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeEvent.Noop)
      .assertValueAt(1,
        EmployeeEvent.Snackbar(
          vm = SnackbarModel(
            messageResId = R.string.get_employees_network_error,
            duration = Snackbar.LENGTH_LONG
          )
        )
      )
      .assertValueAt(2, EmployeeEvent.Noop)
  }

  @Test
  fun `on Get Employees action when not found error`() {
    // GIVEN
    val getEmployeesResponse = Single.just(GetEmployeesResponse.NotFound as GetEmployeesResponse)
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = getEmployeesResponse)
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = employeeViewModel.stateObs().test()
    val eventObs = employeeViewModel.eventObs().test()
    actionSignal.onNext(EmployeeAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeState.ProgressVisibility(visibility = Visibility.VISIBLE))
      .assertValueAt(1, EmployeeState.EmptyState)
      .assertValueAt(2, EmployeeState.ProgressVisibility(visibility = Visibility.GONE))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeEvent.Noop)
      .assertValueAt(1,
        EmployeeEvent.Snackbar(
          vm = SnackbarModel(
            messageResId = R.string.get_employees_not_found_error,
            duration = Snackbar.LENGTH_LONG
          )
        )
      )
      .assertValueAt(2, EmployeeEvent.Noop)
  }

  @Test
  fun `on Get Employees action when Unknown error`() {
    // GIVEN
    val getEmployeesResponse = Single.just(
      GetEmployeesResponse.UnknownError(RuntimeException("Something went wrong")) as GetEmployeesResponse)
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = getEmployeesResponse)
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = employeeViewModel.stateObs().test()
    val eventObs = employeeViewModel.eventObs().test()
    actionSignal.onNext(EmployeeAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeState.ProgressVisibility(visibility = Visibility.VISIBLE))
      .assertValueAt(1, EmployeeState.EmptyState)
      .assertValueAt(2, EmployeeState.ProgressVisibility(visibility = Visibility.GONE))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(3)
      .assertValueAt(0, EmployeeEvent.Noop)
      .assertValueAt(1,
        EmployeeEvent.Snackbar(
          vm = SnackbarModel(
            messageResId = R.string.get_employees_unknown_error,
            duration = Snackbar.LENGTH_LONG
          )
        )
      )
      .assertValueAt(2, EmployeeEvent.Noop)
  }

  @Test
  fun `sortAndMapEmployee`() {
    // GIVEN
    val fakeEmployeeUseCase = FakeEmployeeUseCase(getEmployeesResponse = Single.error(IllegalStateException("Network call not made!")))
    val employeeViewModel = EmployeeViewModel(employeeUseCase = fakeEmployeeUseCase)
    val actionSignal = PublishSubject.create<EmployeeAction>()
    employeeViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val employeeItemsSortedByFullName = employeeViewModel.sortAndMapEmployee(EmployeeDataProvider.employeeList)

    // THEN
    Truth.assertThat(employeeItemsSortedByFullName).isEqualTo(EmployeeDataProvider.employeeItemsSortedByFullname)
  }
}