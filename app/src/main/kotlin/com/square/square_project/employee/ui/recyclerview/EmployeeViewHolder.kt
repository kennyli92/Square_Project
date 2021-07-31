package com.square.square_project.employee.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.square.square_project.R
import com.square.square_project.databinding.ItemViewEmployeeBinding

class EmployeeViewHolder(
  private val binding: ItemViewEmployeeBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(item: EmployeeItem) {
    if (item.imageUrl.isNotBlank()) {
      Glide.with(binding.root)
        .load(item.imageUrl)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(R.drawable.ic_outline_person)
        .error(R.drawable.ic_outline_person)
        .into(binding.employeeImage)
    }

    binding.employeeFullName.text = item.fullName
    binding.employeeTeam.text = item.team
  }
}