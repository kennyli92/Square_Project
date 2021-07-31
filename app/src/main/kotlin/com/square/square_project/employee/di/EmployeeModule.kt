package com.square.square_project.employee.di

import com.square.square_project.employee.data.EmployeeRepository
import com.square.square_project.employee.network.EmployeeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object EmployeeModule {
  @ViewModelScoped
  @Provides
  fun providesEmployeeApi(
    retrofit: Retrofit
  ): EmployeeApi {
    return retrofit.create(EmployeeApi::class.java)
  }

  @ViewModelScoped
  @Provides
  fun providesEmployeeRepository(
    employeeApi: EmployeeApi
  ): EmployeeRepository {
    return EmployeeRepository(employeeApi = employeeApi)
  }
}