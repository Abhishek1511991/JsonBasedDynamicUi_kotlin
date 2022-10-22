package com.demo.dynamicjsonui.dynamicui.interfaces

import com.demo.dynamicjsonui.dynamicui.views.MvpView

interface MvpPresenter<V : MvpView?> {
    fun attachView(view: V)
    fun detachView(retainInstance: Boolean)
}