package com.square.square_project.employee.ui

import com.square.square_project.utils.SnackbarModel

sealed class EmployeeEvent {
  object Noop: EmployeeEvent()
  data class Snackbar(
    val vm: SnackbarModel
  ) : EmployeeEvent()
}