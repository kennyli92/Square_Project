package com.square.square_project.employee.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.square.square_project.R
import com.square.square_project.databinding.ItemViewEmployeeBinding

class EmployeeAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var items: List<EmployeeItem> = emptyList()

  override fun getItemCount(): Int {
    return items.size
  }

  override fun getItemViewType(position: Int): Int {
    return R.layout.item_view_employee
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as EmployeeViewHolder).bind(items[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)

    return EmployeeViewHolder(
      binding = ItemViewEmployeeBinding.inflate(inflater, parent, false)
    )
  }
}