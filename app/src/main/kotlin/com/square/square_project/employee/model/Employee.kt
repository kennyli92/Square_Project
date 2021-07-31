package com.square.square_project.employee.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Employee(
  val uuid: String,
  @Json(name = "full_name")
  val fullName: String,
  @Json(name = "phone_number")
  val phoneNumber: String = "",
  @Json(name = "email_address")
  val emailAddress: String,
  val biography: String = "",
  @Json(name = "photo_url_small")
  val photoUrlSmall: String = "",
  @Json(name = "photo_url_large")
  val photoUrlLarge: String = "",
  val team: String,
  @Json(name = "employee_type")
  val employeeType: EmployeeType
)

enum class EmployeeType {
  FULL_TIME,
  PART_TIME,
  CONTRACTOR
}
