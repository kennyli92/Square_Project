package com.square.square_project.utils

import com.google.android.material.snackbar.Snackbar

data class SnackbarModel(
  val messageResId: Int = 0,
  val message: String = "",
  val duration: Int = Snackbar.LENGTH_SHORT,
  val actionTextResId: Int = 0,
  val actionText: String = "",
  val action: () -> Unit = {}
)