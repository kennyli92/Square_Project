package com.square.square_project.utils

data class StateEvent<out S, out E>(
  val state: S,
  val event: E
)