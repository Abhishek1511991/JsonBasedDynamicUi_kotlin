package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.TextUtils
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.custom_views.DynamicElementCreator
import com.demo.dynamicjsonui.dynamicui.custom_views.MultiTextWatcher
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.rey.material.util.ViewUtil
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by Abhishek at 18/10/2022
 */
class DateTextFactory : FormWidgetFactory {
    var counter = 0

    companion object {
        const val MIN_LENGTH = 0
        const val MAX_LENGTH = 100
        var validator = ArrayList<JSONObject>()
        fun validate(childAt: LinearLayout, focus: Boolean): ValidationStatus {
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val multi_container =
                childAt.findViewById<View>(R.id.multi_container_edit) as LinearLayout
            val count = multi_container.childCount
            for (i in 0 until count) {
                if ((multi_container.getChildAt(i) as EditText).text.toString().isEmpty()) {

                    // ((EditText) multi_container.getChildAt(i)).setError((String) childAt.getTag(R.id.error));
                    if (focus) {
                        (multi_container.getChildAt(i) as EditText).requestFocus()
                    }

                    //return new ValidationStatus(false, (String) childAt.getTag(R.id.error));
                    val errorMsg = childAt.getTag(R.id.error) as String
                    return if (errorMsg != null && !errorMsg.isEmpty()) ValidationStatus(
                        false,
                        childAt.getTag(R.id.error) as String
                    ) else ValidationStatus(
                        false,
                        ""
                    )
                }
            }
            return ValidationStatus(true, null)
        }

        fun isValidEmail(target: CharSequence?): Boolean {
            return if (target == null) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }
    }

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val main_container =
            linear_container.findViewById<View>(R.id.multi_container_edit) as LinearLayout
        val add_btn = linear_container.findViewById<View>(R.id.add_btn) as ImageView
        if (jsonObject?.has("mi") == true) {
            val singleMultiple = jsonObject.getString("mi")
            if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                add_btn.visibility = View.VISIBLE
            } else {
                add_btn.visibility = View.GONE
            }
        } else {
            add_btn.visibility = View.GONE
        }
        if (jsonObject?.has("disabled") == true) {
            add_btn.visibility = View.GONE
        }
        if (jsonObject?.has("openInstance") == true) {
            add_btn.visibility = View.GONE
        }
        if (jsonObject?.has("pendingMulti") == true) {
            if (jsonObject.has("mi") == true) {
                val singleMultiple = jsonObject.getString("mi")
                if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                    add_btn.visibility = View.VISIBLE
                } else {
                    add_btn.visibility = View.GONE
                }
            }
        }
        val multi_lable = linear_container.findViewById<View>(R.id.multi_lable) as TextView
        multi_lable.text = jsonObject?.getString("title")
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val jsonArray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    for (x in 0 until jsonArray.length()) {
                        counter++
                        val edttextitem = EditText(context)
                        edttextitem.isFocusableInTouchMode = true
                        if (jsonObject.has("disabled")) {
                            edttextitem.isEnabled = false
                            edttextitem.setTextColor(Color.BLACK)
                        }
                        edttextitem.layoutParams = params
                        context?.resources?.getColor(R.color.grey)
                            ?.let { edttextitem.setHintTextColor(it) }
                        context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                            edttextitem.setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                it
                            )
                        }
                        edttextitem.inputType = InputType.TYPE_NULL
                        try {
                            edttextitem.setTag(R.id.key, jsonArray.getJSONObject(x).getString("id"))
                            edttextitem.setTag(R.id.type, jsonObject.getString("type"))
                            if (jsonObject.has("placeholder")) {
                                edttextitem.hint = jsonObject.getString("placeholder")
                            }
                            if (jsonArray.getJSONObject(x).has("defaultValue")) {
                                edttextitem.setText(
                                    jsonArray.getJSONObject(x).getString("defaultValue")
                                )
                            }
                            if (jsonArray.getJSONObject(x).has("val")) {
                                edttextitem.setText(jsonArray.getJSONObject(x).getString("val"))
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        edttextitem.addTextChangedListener(
                            MultiTextWatcher(
                                stepName,
                                linear_container,
                                edttextitem
                            )
                        )
                        edttextitem.onFocusChangeListener = listener
                        edttextitem.setOnClickListener(listener)
                        main_container.addView(edttextitem)
                    }
                } else {
                    counter++
                    val edttext = EditText(context)
                    edttext.isFocusableInTouchMode = true
                    if (jsonObject.has("disabled")) {
                        edttext.isEnabled = false
                        edttext.setTextColor(Color.BLACK)
                    }
                    edttext.layoutParams = params
                    edttext.inputType = InputType.TYPE_NULL
                    context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                        edttext.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            it
                        )
                    }
                    context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
                    try {
                        edttext.id = ViewUtil.generateViewId()
                        edttext.setTag(R.id.key, jsonObject.getString("id") + "_" + counter)
                        edttext.setTag(R.id.type, jsonObject.getString("type"))
                        if (jsonObject.has("placeholder")) {
                            edttext.hint = jsonObject.getString("placeholder")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    DynamicElementCreator(stepName, linear_container, edttext)
                    edttext.addTextChangedListener(
                        MultiTextWatcher(
                            stepName,
                            linear_container,
                            edttext
                        )
                    )
                    edttext.onFocusChangeListener = listener
                    main_container.addView(edttext)
                    if (!TextUtils.isEmpty(jsonObject.optString("disabled"))) {
                        edttext.isEnabled = false
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("defaultValue"))) {
                        edttext.setText(jsonObject.optString("defaultValue"))
                    }
                }
            }
        } else {
            counter++
            val edttext = EditText(context)
            edttext.isFocusableInTouchMode = true
            if (jsonObject?.has("disabled") == true) {
                edttext.isEnabled = false
                edttext.setTextColor(Color.BLACK)
            }
            edttext.layoutParams = params
            edttext.inputType = InputType.TYPE_NULL
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                edttext.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it
                )
            }
            context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
            try {
                edttext.id = ViewUtil.generateViewId()
                edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                edttext.setTag(R.id.type, jsonObject?.getString("type"))
                if (jsonObject?.has("placeholder") == true) {
                    edttext.hint = jsonObject.getString("placeholder")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            DynamicElementCreator(stepName, linear_container, edttext)
            edttext.addTextChangedListener(MultiTextWatcher(stepName, linear_container, edttext))
            edttext.onFocusChangeListener = listener
            main_container.addView(edttext)
            if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                edttext.isEnabled = false
            }
            if (!TextUtils.isEmpty(jsonObject?.optString("defaultValue"))) {
                edttext.setText(jsonObject?.optString("defaultValue"))
            }
        }
        add_btn.setOnClickListener {
            counter++
            val edttextitemInner = EditText(context)
            edttextitemInner.isFocusableInTouchMode = true
            if (jsonObject?.has("disabled") == true) {
                //  edttextitemInner.setEnabled(false);
            }
            edttextitemInner.inputType = InputType.TYPE_NULL
            edttextitemInner.layoutParams = params
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let { it1 ->
                edttextitemInner.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it1
                )
            }
            context?.resources?.getColor(R.color.grey)
                ?.let { it1 -> edttextitemInner.setHintTextColor(it1) }
            try {
                edttextitemInner.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                edttextitemInner.setTag(R.id.type, jsonObject?.getString("type"))
                if (jsonObject?.has("placeholder") == true) {
                    edttextitemInner.hint = jsonObject.getString("placeholder")
                }
                if (jsonObject?.has("type") == true) {
                    edttextitemInner.setTag(R.id.type, jsonObject.getString("type"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            DynamicElementCreator(stepName, linear_container, edttextitemInner)
            edttextitemInner.addTextChangedListener(
                MultiTextWatcher(
                    stepName,
                    linear_container,
                    edttextitemInner
                )
            )
            edttextitemInner.onFocusChangeListener = listener
            main_container.addView(edttextitemInner)
        }
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                linear_container.setTag(R.id.v_required, requiredValue)
                linear_container.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        val emailObject = jsonObject?.optJSONObject("v_email")
        if (emailObject != null) {
            val emailValue = emailObject.getString("required")
            if (!TextUtils.isEmpty(emailValue)) {
                if (java.lang.Boolean.TRUE.toString().equals(emailValue, ignoreCase = true)) {
                    val emailObj = JSONObject()
                    emailObj.put("validator", "email")
                    emailObj.put("error", emailObject.getString("err"))
                    validator.add(emailObject)
                }
            }
        }
        views.add(linear_container)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View  {
        counter = 0
        val linear_container =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val main_container =
            linear_container.findViewById<View>(R.id.multi_container_edit) as LinearLayout
        val add_btn = linear_container.findViewById<View>(R.id.add_btn) as ImageView
        if (jsonObject?.has("mi") == true) {
            val singleMultiple = jsonObject.getString("mi")
            if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                add_btn.visibility = View.VISIBLE
            } else {
                add_btn.visibility = View.GONE
            }
        } else {
            add_btn.visibility = View.GONE
        }
        if (jsonObject?.has("disabled") == true) {
            add_btn.visibility = View.GONE
        }
        if (jsonObject?.has("openInstance") == true) {
            add_btn.visibility = View.GONE
        }
        if (jsonObject?.has("pendingMulti") == true) {
            if (jsonObject.has("mi") == true) {
                val singleMultiple = jsonObject.getString("mi")
                if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                    add_btn.visibility = View.VISIBLE
                } else {
                    add_btn.visibility = View.GONE
                }
            }
        }
        val multi_lable = linear_container.findViewById<View>(R.id.multi_lable) as TextView
        multi_lable.text = jsonObject?.getString("title")
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val jsonArray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    for (x in 0 until jsonArray.length()) {
                        counter++
                        val edttextitem = EditText(context)
                        edttextitem.isFocusableInTouchMode = true
                        if (jsonObject.has("disabled")) {
                            edttextitem.isEnabled = false
                            edttextitem.setTextColor(Color.BLACK)
                        }
                        edttextitem.layoutParams = params
                        context?.resources?.getColor(R.color.grey)
                            ?.let { edttextitem.setHintTextColor(it) }
                        context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                            edttextitem.setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                it
                            )
                        }
                        edttextitem.inputType = InputType.TYPE_NULL
                        try {
                            edttextitem.setTag(R.id.key, jsonArray.getJSONObject(x).getString("id"))
                            edttextitem.setTag(R.id.type, jsonObject.getString("type"))
                            if (jsonObject.has("placeholder")) {
                                edttextitem.hint = jsonObject.getString("placeholder")
                            }
                            if (jsonArray.getJSONObject(x).has("defaultValue")) {
                                edttextitem.setText(
                                    jsonArray.getJSONObject(x).getString("defaultValue")
                                )
                            }
                            if (jsonArray.getJSONObject(x).has("val")) {
                                edttextitem.setText(jsonArray.getJSONObject(x).getString("val"))
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        edttextitem.addTextChangedListener(
                            MultiTextWatcher(
                                stepName,
                                linear_container,
                                edttextitem
                            )
                        )
                        edttextitem.onFocusChangeListener = listener
                        edttextitem.setOnClickListener(listener)
                        main_container.addView(edttextitem)
                    }
                } else {
                    counter++
                    val edttext = EditText(context)
                    edttext.isFocusableInTouchMode = true
                    if (jsonObject.has("disabled")) {
                        edttext.isEnabled = false
                        edttext.setTextColor(Color.BLACK)
                    }
                    edttext.layoutParams = params
                    edttext.inputType = InputType.TYPE_NULL
                    context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                        edttext.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            it
                        )
                    }
                    context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
                    try {
                        edttext.id = ViewUtil.generateViewId()
                        edttext.setTag(R.id.key, jsonObject.getString("id") + "_" + counter)
                        edttext.setTag(R.id.type, jsonObject.getString("type"))
                        if (jsonObject.has("placeholder")) {
                            edttext.hint = jsonObject.getString("placeholder")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    DynamicElementCreator(stepName, linear_container, edttext)
                    edttext.addTextChangedListener(
                        MultiTextWatcher(
                            stepName,
                            linear_container,
                            edttext
                        )
                    )
                    edttext.onFocusChangeListener = listener
                    main_container.addView(edttext)
                    if (!TextUtils.isEmpty(jsonObject.optString("disabled"))) {
                        edttext.isEnabled = false
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("defaultValue"))) {
                        edttext.setText(jsonObject.optString("defaultValue"))
                    }
                }
            }
        } else {
            counter++
            val edttext = EditText(context)
            edttext.isFocusableInTouchMode = true
            if (jsonObject?.has("disabled") == true) {
                edttext.isEnabled = false
                edttext.setTextColor(Color.BLACK)
            }
            edttext.layoutParams = params
            edttext.inputType = InputType.TYPE_NULL
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                edttext.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it
                )
            }
            context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
            try {
                edttext.id = ViewUtil.generateViewId()
                edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                edttext.setTag(R.id.type, jsonObject?.getString("type"))
                if (jsonObject?.has("placeholder") == true) {
                    edttext.hint = jsonObject.getString("placeholder")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            DynamicElementCreator(stepName, linear_container, edttext)
            edttext.addTextChangedListener(MultiTextWatcher(stepName, linear_container, edttext))
            edttext.onFocusChangeListener = listener
            main_container.addView(edttext)
            if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                edttext.isEnabled = false
            }
            if (!TextUtils.isEmpty(jsonObject?.optString("defaultValue"))) {
                edttext.setText(jsonObject?.optString("defaultValue"))
            }
        }
        add_btn.setOnClickListener {
            counter++
            val edttextitemInner = EditText(context)
            edttextitemInner.isFocusableInTouchMode = true
            if (jsonObject?.has("disabled") == true) {
                //  edttextitemInner.setEnabled(false);
            }
            edttextitemInner.inputType = InputType.TYPE_NULL
            edttextitemInner.layoutParams = params
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let { it1 ->
                edttextitemInner.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it1
                )
            }
            context?.resources?.getColor(R.color.grey)
                ?.let { it1 -> edttextitemInner.setHintTextColor(it1) }
            try {
                edttextitemInner.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                edttextitemInner.setTag(R.id.type, jsonObject?.getString("type"))
                if (jsonObject?.has("placeholder") == true) {
                    edttextitemInner.hint = jsonObject.getString("placeholder")
                }
                if (jsonObject?.has("type") == true) {
                    edttextitemInner.setTag(R.id.type, jsonObject.getString("type"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            DynamicElementCreator(stepName, linear_container, edttextitemInner)
            edttextitemInner.addTextChangedListener(
                MultiTextWatcher(
                    stepName,
                    linear_container,
                    edttextitemInner
                )
            )
            edttextitemInner.onFocusChangeListener = listener
            main_container.addView(edttextitemInner)
        }
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                linear_container.setTag(R.id.v_required, requiredValue)
                linear_container.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        val emailObject = jsonObject?.optJSONObject("v_email")
        if (emailObject != null) {
            val emailValue = emailObject.getString("required")
            if (!TextUtils.isEmpty(emailValue)) {
                if (java.lang.Boolean.TRUE.toString().equals(emailValue, ignoreCase = true)) {
                    val emailObj = JSONObject()
                    emailObj.put("validator", "email")
                    emailObj.put("error", emailObject.getString("err"))
                    validator.add(emailObject)
                }
            }
        }
        return linear_container
    }
}