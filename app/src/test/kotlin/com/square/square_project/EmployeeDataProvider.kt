package com.square.square_project

import com.square.square_project.employee.model.Employee
import com.square.square_project.employee.model.EmployeeType
import com.square.square_project.employee.model.Employees
import com.square.square_project.employee.ui.recyclerview.EmployeeItem

object EmployeeDataProvider {
  val employee1 = Employee(
    uuid = "1",
    fullName = "Zack Doe",
    emailAddress = "first@square.com",
    biography = "",
    photoUrlSmall = "first small url",
    photoUrlLarge = "first large url",
    team = "team 1",
    employeeType = EmployeeType.FULL_TIME
  )
  val employee2 = Employee(
    uuid = "2",
    fullName = "John Doe",
    emailAddress = "second@square.com",
    biography = "",
    photoUrlSmall = "second small url",
    photoUrlLarge = "second large url",
    team = "team 2",
    employeeType = EmployeeType.PART_TIME
  )
  val employee3 = Employee(
    uuid = "3",
    fullName = "Jane Doe",
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

  val employeeItem1 = EmployeeItem(
    imageUrl = "first small url",
    fullName = "Zack Doe",
    team = "team 1"
  )
  val employeeItem2 = EmployeeItem(
    imageUrl = "second small url",
    fullName = "John Doe",
    team = "team 2"
  )
  val employeeItem3 = EmployeeItem(
    imageUrl = "third small url",
    fullName = "Jane Doe",
    team = "team 3"
  )
  val employeeItemsSortedByFullname = listOf(employeeItem3, employeeItem2, employeeItem1)
}