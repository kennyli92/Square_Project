package com.square.square_project.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Operator overload provideDelegate to initialize [disposables] property when getValue()
 * is called by the LifecycleOwner. Dispose and set [disposables] to null onDestroy().
 */
class DisposableOnDestroy :
  ReadOnlyProperty<LifecycleOwner, CompositeDisposable>,
  DefaultLifecycleObserver {

  private var disposables: CompositeDisposable? = null

  override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): CompositeDisposable {
    if (disposables == null) {
      val lifecycle = when (thisRef) {
        is Fragment -> thisRef.viewLifecycleOwner.lifecycle
        else -> thisRef.lifecycle
      }

      lifecycle.addObserver(this)

      disposables = CompositeDisposable()
    }
    return disposables!!
  }

  override fun onDestroy(owner: LifecycleOwner) {
    super.onDestroy(owner)
    disposables?.dispose()
    disposables = null
    owner.lifecycle.removeObserver(this)
  }
}