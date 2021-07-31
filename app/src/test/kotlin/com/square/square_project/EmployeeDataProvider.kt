package com.square.square_project

import com.square.square_project.employee.model.Employee
import com.square.square_project.employee.model.EmployeeType
import com.square.square_project.employee.model.Employees

object EmployeeDataProvider {
  val employee1 = Employee(
    uuid = "1",
    fullName = "first employee",
    emailAddress = "first@square.com",
    biography = "",
    photoUrlSmall = "first small url",
    photoUrlLarge = "first large url",
    team = "team 1",
    employeeType = EmployeeType.FULL_TIME
  )
  val employee2 = Employee(
    uuid = "2",
    fullName = "second employee",
    emailAddress = "second@square.com",
    biography = "",
    photoUrlSmall = "second small url",
    photoUrlLarge = "second large url",
    team = "team 2",
    employeeType = EmployeeType.PART_TIME
  )
  val employee3 = Employee(
    uuid = "3",
    fullName = "third employee",
    emailAddress = "third@square.com",
    biography = "",
    photoUrlSmall = "third small url",
    photoUrlLarge = "third large url",
    team = "team 3",
    employeeType = EmployeeType.CONTRACTOR
  )
  val employeeList = listOf(employee1, employee2, employee3)
  val employees = Employees(
    employees = employeeList
  )
}