package com.square.square_project.employee.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Employees(
  val employees: List<Employee>
)
