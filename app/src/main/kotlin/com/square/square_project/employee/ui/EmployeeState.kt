package com.square.square_project.employee.ui

import com.square.square_project.employee.model.Employee

sealed class EmployeeState {
  object Noop: EmployeeState()
  data class ListItem(
    val employees: List<Employee>
  ) : EmployeeState()
}