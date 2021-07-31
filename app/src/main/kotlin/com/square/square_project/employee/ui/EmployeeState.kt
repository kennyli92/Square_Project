package com.square.square_project.employee.ui

import com.square.square_project.employee.ui.recyclerview.EmployeeItem
import com.square.square_project.utils.Visibility

sealed class EmployeeState {
  object Noop: EmployeeState()
  data class ListItem(
    val employeeItems: List<EmployeeItem>
  ) : EmployeeState()
  object EmptyState : EmployeeState()
  data class ProgressVisibility(
    val visibility: Visibility
  ): EmployeeState()
}