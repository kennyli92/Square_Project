package com.square.square_project.extensions

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

infix operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
  add(disposable)
}