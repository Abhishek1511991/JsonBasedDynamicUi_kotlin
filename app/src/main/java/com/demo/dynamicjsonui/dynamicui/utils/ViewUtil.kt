package com.demo.dynamicjsonui.dynamicui.utils

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import java.util.concurrent.atomic.AtomicInteger


object ViewUtil {

    private val sNextGeneratedId: AtomicInteger = AtomicInteger(1)

    @SuppressLint("NewApi")
    fun generateViewId(): Int {
        if (Build.VERSION.SDK_INT < 17) {
            while (true) {
                val result: Int = sNextGeneratedId.get()
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                var newValue = result + 1
                if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result
                }
            }
        } else {
            return View.generateViewId()
        }
    }
}