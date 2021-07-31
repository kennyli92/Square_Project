package com.square.square_project.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Operator overload provideDelegate to set view to null when destroy lifecycle event
 * is observed on the property host object (LifecycleOwner).
 */
class ClearViewOnDestroy<T : Any>(val onDestroyAction: (() -> Unit)? = null) :
  ReadWriteProperty<LifecycleOwner, T>,
  DefaultLifecycleObserver {
  private var dependencyValue: T? = null

  // Throw error if dependency injection failed to set dependency value
  override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
    return dependencyValue ?: throw UninitializedPropertyAccessException(property.name)
  }

  override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
    val lifecycle = when (thisRef) {
      is Fragment -> thisRef.viewLifecycleOwner.lifecycle
      else -> thisRef.lifecycle
    }
    lifecycle.addObserver(this)
    dependencyValue = value
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    onDestroyAction?.invoke()
    dependencyValue = null
    owner.lifecycle.removeObserver(this)
  }
}