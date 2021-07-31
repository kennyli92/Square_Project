package com.square.square_project.employee.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.square.square_project.employee.data.EmployeeUseCaseImpl
import com.square.square_project.employee.model.Employee
import com.square.square_project.utils.StateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
  private val employeeUseCase: EmployeeUseCaseImpl
) : ViewModel() {
  private val stateEventObs =
    PublishSubject.create<StateEvent<EmployeeState, EmployeeEvent>>().toSerialized()

  private val employees: MutableList<Employee> = mutableListOf()

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
    return Observable.just(StateEvent(EmployeeState.Noop, EmployeeEvent.Noop))
  }

  /**
   * Get latest weather conditions of the present and next 5 days, and display std dev
   */
  private fun onGetEmployees(): Observable<StateEvent<EmployeeState, EmployeeEvent>> {
    return Observable.just(StateEvent(EmployeeState.Noop, EmployeeEvent.Noop))
  }
}