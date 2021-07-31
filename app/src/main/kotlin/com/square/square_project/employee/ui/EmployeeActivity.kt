package com.square.square_project.employee.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.square.square_project.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_employee)
  }
}