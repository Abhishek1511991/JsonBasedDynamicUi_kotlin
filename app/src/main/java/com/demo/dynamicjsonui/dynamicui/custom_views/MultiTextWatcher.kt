package com.demo.dynamicjsonui.dynamicui.custom_views

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.TintContextWrapper
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.interfaces.JsonApi
import org.json.JSONException

class MultiTextWatcher(
    private val mStepName: String?,
    private val mView: View,
    private val pView: View
) : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        val text: String = editable.toString()
        var api: JsonApi? = null
        api = if (mView.context is JsonApi) {
            mView.context as JsonApi
        } else if (mView.context is TintContextWrapper) {
            val tintContextWrapper: TintContextWrapper = mView.context as TintContextWrapper
            tintContextWrapper.getBaseContext() as JsonApi
        } else {
            throw RuntimeException("Could not fetch context")
        }
        val key = mView.getTag(R.id.key) as String
        val pkey = pView.getTag(R.id.key) as String
        try {
            api.writeValue(mStepName, key, JsonFormConstants.OPTIONS_FIELD_NAME, pkey, text)
        } catch (e: JSONException) {
            // TODO- handle
            e.printStackTrace()
        }
    }
}