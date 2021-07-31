package com.square.square_project.employee.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.square.square_project.R
import com.square.square_project.employee.data.EmployeeUseCase
import com.square.square_project.employee.model.Employee
import com.square.square_project.employee.model.GetEmployeesResponse
import com.square.square_project.utils.SnackbarModel
import com.square.square_project.utils.StateEvent
import com.square.square_project.utils.Visibility
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
  private val employeeUseCase: EmployeeUseCase
) : ViewModel() {
  private val stateEventObs =
    PublishSubject.create<StateEvent<EmployeeState, EmployeeEvent>>().toSerialized()

  private var employees: MutableList<Employee> = mutableListOf()

  /**
   * emit State that should persist on screen upon foreground from backgrounding
   */
  fun stateObs(): Observable<EmployeeState> {
    return stateEventObs.map {
      it.state
    }.distinctUntilChanged().observeOn(AndroidSchedulers.mainThread())
  }

  /**
   * Emit single Event that should happen once and not persist on the screen such as dialogs,
   * snackbars, navigations
   */
  fun eventObs(): Observable<EmployeeEvent> {
    return stateEventObs.map {
      it.event
    }.observeOn(AndroidSchedulers.mainThread())
  }

  /**
   * Observe actions to be handled created from click streams
   */
  fun actionHandler(actionSignal: Observable<EmployeeAction>): Disposable {
    return actionSignal
      .observeOn(Schedulers.computation())
      .flatMap { action ->
        when (action) {
          is EmployeeAction.Load -> onLoad()
          is EmployeeAction.GetEmployees -> onGetEmployees()
        }
      }.subscribe({
        stateEventObs.onNext(it)
      }, {
        Log.e("EmployeeViewModel Error", it.message ?: "")
      })
  }

  /**
   * Initial data load to populate UI
   */
  private fun onLoad(): Observable<StateEvent<EmployeeState, EmployeeEvent>> {
    return if (employees.isNotEmpty()) {
      Observable.just(
        StateEvent(
          EmployeeState.ListItem(
            employees = employees
          ),
          EmployeeEvent.Noop
        )
      )
    } else {
      onGetEmployees()
    }
  }

  /**
   * Get latest weather conditions of the present and next 5 days, and display std dev
   */
  private fun onGetEmployees(): Observable<StateEvent<EmployeeState, EmployeeEvent>> {
    return employeeUseCase.getEmployees()
      .flatMapObservable { response ->
        when(response) {
          is GetEmployeesResponse.Employees -> {
            employees = response.employees.toMutableList()
            Observable.just(
              StateEvent(
                EmployeeState.ListItem(
                  employees = response.employees
                ),
                EmployeeEvent.Noop
              ),
              StateEvent(
                EmployeeState.ProgressVisibility(visibility = Visibility.GONE),
                EmployeeEvent.Noop
              )
            )
          }
          is GetEmployeesResponse.NetworkError -> {
            Observable.just(
              StateEvent(
                EmployeeState.Noop,
                EmployeeEvent.Snackbar(
                  vm = SnackbarModel(
                    messageResId = R.string.get_employees_network_error,
                    duration = Snackbar.LENGTH_LONG
                  )
                )
              ),
              StateEvent(
                EmployeeState.ProgressVisibility(visibility = Visibility.GONE),
                EmployeeEvent.Noop
              )
            )
          }
          is GetEmployeesResponse.NotFound -> {
            Observable.just(
              StateEvent(
                EmployeeState.Noop,
                EmployeeEvent.Snackbar(
                  vm = SnackbarModel(
                    messageResId = R.string.get_employees_not_found_error,
                    duration = Snackbar.LENGTH_LONG
                  )
                )
              ),
              StateEvent(
                EmployeeState.ProgressVisibility(visibility = Visibility.GONE),
                EmployeeEvent.Noop
              )
            )
          }
          is GetEmployeesResponse.UnknownError -> {
            Observable.just(
              StateEvent(
                EmployeeState.Noop,
                EmployeeEvent.Snackbar(
                  vm = SnackbarModel(
                    messageResId = R.string.get_employees_unknown_error,
                    duration = Snackbar.LENGTH_LONG
                  )
                )
              ),
              StateEvent(
                EmployeeState.ProgressVisibility(visibility = Visibility.GONE),
                EmployeeEvent.Noop
              )
            )
          }
        }
      }.startWith (
        Observable.just(
          StateEvent(
            EmployeeState.ProgressVisibility(
              visibility = Visibility.VISIBLE
            ),
            EmployeeEvent.Noop
          ))
      )
  }
}