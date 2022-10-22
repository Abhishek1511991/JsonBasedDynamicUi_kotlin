package com.demo.dynamicjsonui.dynamicui.viewState

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
open class ViewState: Parcelable {

    private var mIsSavedInstance = false

    fun isSavedInstance(): Boolean {
        return mIsSavedInstance
    }

    fun setSavedInstance(isSavedInstance: Boolean) {
        mIsSavedInstance = isSavedInstance
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (mIsSavedInstance) 1.toByte() else 0.toByte())
    }

    fun ViewState() {}

    protected fun ViewState(`in`: Parcel) {
        mIsSavedInstance = `in`.readByte().toInt() != 0
    }
}