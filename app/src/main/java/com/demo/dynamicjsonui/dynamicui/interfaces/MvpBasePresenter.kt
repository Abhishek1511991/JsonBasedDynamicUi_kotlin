package com.demo.dynamicjsonui.dynamicui.interfaces

import com.demo.dynamicjsonui.dynamicui.views.MvpView
import java.lang.ref.WeakReference

open class MvpBasePresenter<V : MvpView?> : MvpPresenter<V> {
    private var viewRef: WeakReference<V?>? = null
    override fun attachView(view: V) {
        viewRef = WeakReference(view)
    }

    /**
     * Get the attached view. You should always call [.isViewAttached]
     * to check if the view is attached to avoid NullPointerExceptions
     */
    protected val view: V?
        get() = viewRef!!.get()

    /**
     * Checks if a view is attached to this presenter. You should always call
     * this method before calling [.getView] to get the view instance.
     */
    protected val isViewAttached: Boolean
        get() = viewRef != null && viewRef!!.get() != null

    override fun detachView(retainInstance: Boolean) {
        if (viewRef != null) {
            viewRef!!.clear()
            viewRef = null
        }
    }
}