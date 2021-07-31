package com.square.square_project.employee.ui

sealed class EmployeeAction {
  object Load : EmployeeAction()
  object GetEmployees : EmployeeAction()
}