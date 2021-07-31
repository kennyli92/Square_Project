package com.square.square_project.employee.ui

import com.square.square_project.employee.model.Employee
import com.square.square_project.utils.Visibility

sealed class EmployeeState {
  object Noop: EmployeeState()
  data class ListItem(
    val employees: List<Employee>
  ) : EmployeeState()
  data class ProgressVisibility(
    val visibility: Visibility
  ): EmployeeState()
}