package com.demo.dynamicjsonui.dynamicui.custom_views

import android.view.View
import androidx.appcompat.widget.TintContextWrapper
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.interfaces.JsonApi
import org.json.JSONException
import java.lang.RuntimeException

class CheckboxDynamicElementCreator {
    private var mView: View
    private var pView: View? = null
    private var mStepName: String?
    var parentkey: String? = null
    var childkey: String? = null
    var value: String? = null

    constructor(stepName: String?, view: View, pView: View?) {
        mView = view
        mStepName = stepName
        this.pView = pView
        cteatorChanged()
    }

    constructor(stepName: String?, view: View, pkey: String?, ckey: String?, value: String?) {
        mView = view
        mStepName = stepName
        parentkey = pkey
        childkey = ckey
        this.value = value
        cteatorChanged()
    }

    fun cteatorChanged() {
        var api: JsonApi? = null
        api = if (mView.context is JsonApi) {
            mView.context as JsonApi
        } else if (mView.context is TintContextWrapper) {
            val tintContextWrapper: TintContextWrapper = mView.context as TintContextWrapper
            tintContextWrapper.getBaseContext() as JsonApi
        } else {
            throw RuntimeException("Could not fetch context")
        }
        if (parentkey != null && childkey != null) {
            try {
                api.writeValueMulti(
                    mStepName,
                    parentkey,
                    JsonFormConstants.OPTIONS_FIELD_NAME,
                    childkey,
                    value
                )
            } catch (e: JSONException) {
                // TODO- handle
                e.printStackTrace()
            }
        } else {
            val key = mView.getTag(R.id.key) as String
            val pkey = pView!!.getTag(R.id.key) as String
            try {
                api.writeValueMulti(mStepName, key, JsonFormConstants.OPTIONS_FIELD_NAME, pkey, "")
            } catch (e: JSONException) {
                // TODO- handle
                e.printStackTrace()
            }
        }
    }
}