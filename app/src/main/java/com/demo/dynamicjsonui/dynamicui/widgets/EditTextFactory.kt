package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class EditTextFactory : FormWidgetFactory {
    var counter = 0
    
    companion object {
        const val MIN_LENGTH = 0
        const val MAX_LENGTH = 100
        var context: Context? = null
    }

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        val minLength = MIN_LENGTH
        val maxLength = MAX_LENGTH
        counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container: LinearLayout =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        //linear_container.setId(ViewUtil.generateViewId())
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val edttext = EditText(context)
        edttext.setFocusableInTouchMode(true)
        edttext.setLayoutParams(params)
        if (jsonObject?.has("disabled") == true) {
            edttext.setEnabled(false)
            edttext.setTextColor(Color.BLACK)
        }
        context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
            edttext.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                it
            )
        }
        try {
            if (jsonObject?.has("hint") == true) {
                edttext.hint = jsonObject.getString("hint")
                edttext.setHintTextColor(Color.GRAY)
            }
            counter++
            edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
            if (jsonObject?.has("placeholder") == true) {
                edttext.setHint(jsonObject.getString("placeholder"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        linear_container.addView(edttext)
        views.add(linear_container)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        val minLength = MIN_LENGTH
        val maxLength = MAX_LENGTH
        counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container: LinearLayout =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        //linear_container.setId(ViewUtil.generateViewId())
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val edttext = EditText(context)
        edttext.setFocusableInTouchMode(true)
        edttext.setLayoutParams(params)
        if (jsonObject?.has("disabled") == true) {
            edttext.setEnabled(false)
            edttext.setTextColor(Color.BLACK)
        }
        context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
            edttext.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                it
            )
        }
        try {
            if (jsonObject?.has("hint") == true) {
                edttext.hint = jsonObject.getString("hint")
                edttext.setHintTextColor(Color.GRAY)
            }
            counter++
            edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
            if (jsonObject?.has("placeholder") == true) {
                edttext.setHint(jsonObject.getString("placeholder"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        linear_container.addView(edttext)
        return linear_container
    }
}