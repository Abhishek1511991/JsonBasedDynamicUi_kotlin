package com.demo.dynamicjsonui.dynamicui.viewState

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Abhishek at 03/10/2022
 */
class JsonFormFragmentViewState : ViewState, Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
    }

    constructor() {}
    private constructor(`in`: Parcel) : super() {}

    companion object CREATOR : Parcelable.Creator<JsonFormFragmentViewState> {
        override fun createFromParcel(parcel: Parcel): JsonFormFragmentViewState {
            return JsonFormFragmentViewState(parcel)
        }

        override fun newArray(size: Int): Array<JsonFormFragmentViewState?> {
            return arrayOfNulls(size)
        }
    }


}