package com.square.square_project.employee.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import com.square.square_project.R
import com.square.square_project.databinding.ActivityEmployeeBinding
import com.square.square_project.employee.ui.recyclerview.EmployeeAdapter
import com.square.square_project.extensions.plusAssign
import com.square.square_project.extensions.showSnackBar
import com.square.square_project.utils.ClearViewOnDestroy
import com.square.square_project.utils.DisposableOnDestroy
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

@AndroidEntryPoint
class EmployeeActivity : AppCompatActivity() {
  private val vm: EmployeeViewModel by viewModels()
  private var binding: ActivityEmployeeBinding by ClearViewOnDestroy(onDestroyAction = {
    binding.employeeRecyclerView.adapter = null
  })
  private val disposables: CompositeDisposable by DisposableOnDestroy()

  private lateinit var adapter: EmployeeAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEmployeeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // recycler view
    binding.employeeRecyclerView.layoutManager = LinearLayoutManager(this)
    val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    itemDecorator.setDrawable(
      ContextCompat.getDrawable(this, android.R.color.darker_gray)!!)
    binding.employeeRecyclerView.addItemDecoration(itemDecorator)

    adapter = EmployeeAdapter()
    binding.employeeRecyclerView.adapter = adapter

    // STATE
    disposables += vm.stateObs()
      .subscribe({ state ->
        when (state) {
          is EmployeeState.Noop -> {}
          is EmployeeState.ListItem -> onListItemState(state = state)
          is EmployeeState.ProgressVisibility -> onProgressVisibility(state = state)
        }
      }, {
        Log.e("MainActivity Error", it.message ?: "")
      })

    // EVENT
    disposables += vm.eventObs()
      .subscribe({ event ->
        when (event) {
          is EmployeeEvent.Noop -> {}
          is EmployeeEvent.Snackbar -> onSnackbarEvent(event = event)
        }
      }, {
        Log.e("MainActivity Error", it.message ?: "")
      })

    // Action Signals
    val refreshSignal = binding.employeeRefresh.refreshes()
      .map { EmployeeAction.GetEmployees }

    val actionSignal = Observable.merge(
      Observable.just(EmployeeAction.Load),
      refreshSignal
    )

    disposables += vm.actionHandler(actionSignal = actionSignal)
  }

  /** State Handlers **/

  private fun onListItemState(state: EmployeeState.ListItem) {
    adapter.items = state.employeeItems
    adapter.notifyDataSetChanged()

    binding.employeeRefresh.isRefreshing = false
  }

  private fun onProgressVisibility(state: EmployeeState.ProgressVisibility) {
    binding.employeeProgressBar.visibility = state.visibility.value
  }

  /** Event Handlers **/

  private fun onSnackbarEvent(event: EmployeeEvent.Snackbar) {
    binding.root.showSnackBar(snackbarViewModel = event.vm)
  }
}