package com.demo.dynamicjsonui.dynamicui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
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

/**
 * Created by Abhishek at 03/10/2022
 */
class TextAreaFactory : FormWidgetFactory {
    var counter = 0


    var touchListener: View.OnTouchListener = object : View.OnTouchListener {
        override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (motionEvent.getAction() and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            return false
        }
    }

    companion object {
        const val MIN_LENGTH = 0
        const val MAX_LENGTH = 100
        fun validate(childAt: LinearLayout, focus: Boolean): ValidationStatus {
            var isvalidate = false
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val multi_container =
                childAt.findViewById<View>(R.id.multi_container_edit) as LinearLayout
            val count = multi_container.childCount
            for (i in 0 until count) {
                if ((multi_container.getChildAt(i) as EditText).text.toString().isEmpty()) {
                    isvalidate = true
                    //((EditText) multi_container.getChildAt(i)).setError((String) childAt.getTag(R.id.error));
                    if (focus) {
                        (multi_container.getChildAt(i) as EditText).requestFocus()
                    }
                    break
                }
            }
            return if (!isvalidate) {
                ValidationStatus(true, null)
            } else {
                //return new ValidationStatus(false, (String) childAt.getTag(R.id.error));
                val errorMsg = childAt.getTag(R.id.error) as String
                if (errorMsg != null && !errorMsg.isEmpty()) ValidationStatus(
                    false,
                    childAt.getTag(R.id.error) as String
                ) else ValidationStatus(
                    false,
                    ""
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View> {

        counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                linear_container.setTag(R.id.v_required, requiredValue)
                linear_container.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
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
            if (jsonObject.has("mi")) {
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
                        edttextitem.layoutParams = params
                        edttextitem.isVerticalScrollBarEnabled = true
                        context?.resources?.getColor(R.color.grey)
                            ?.let { edttextitem.setHintTextColor(it) }
                        context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                            edttextitem.setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                it
                            )
                        }
                        try {
                            edttextitem.setTag(R.id.key, jsonArray.getJSONObject(x).getString("id"))
                            if (jsonObject.has("disabled")) {
                                edttextitem.isEnabled = false
                                edttextitem.setTextColor(Color.BLACK)
                            } else {
                                edttextitem.setLines(4)
                            }
                            edttextitem.gravity = Gravity.TOP
                            if (jsonObject?.has("placeholder") == true) {
                                edttextitem.hint = jsonObject.getString("placeholder")
                            }
                            if (jsonArray.getJSONObject(x).has("defaultValue")) {
                                val tdval =
                                    jsonArray.getJSONObject(x).getString("defaultValue").toString()
                                        .replace("\$n", "\n")
                                edttextitem.setText(tdval)
                            }
                            if (jsonArray.getJSONObject(x).has("val")) {
                                val tdval = jsonArray.getJSONObject(x).getString("val").toString()
                                    .replace("\$n", "\n")
                                edttextitem.setText(tdval)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        edttextitem.setOnTouchListener(touchListener)
                        edttextitem.addTextChangedListener(
                            MultiTextWatcher(
                                stepName,
                                linear_container,
                                edttextitem
                            )
                        )
                        main_container.addView(edttextitem)
                    }
                } else {
                    counter++
                    val edttext = EditText(context)
                    edttext.isFocusableInTouchMode = true
                    edttext.layoutParams = params
                    edttext.isVerticalScrollBarEnabled = true
                    context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
                    context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                        edttext.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            it
                        )
                    }
                    try {
                        edttext.id = ViewUtil.generateViewId()
                        if (jsonObject.has("disabled")) {
                            edttext.isEnabled = false
                            edttext.setTextColor(Color.BLACK)
                        } else {
                            edttext.setLines(4)
                        }
                        edttext.gravity = Gravity.TOP
                        edttext.setTag(R.id.key, jsonObject.getString("id") + "_" + counter)
                        if (jsonObject.has("placeholder")) {
                            edttext.hint = jsonObject.getString("placeholder")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    edttext.setOnTouchListener(touchListener)
                    DynamicElementCreator(stepName, linear_container, edttext)
                    edttext.addTextChangedListener(
                        MultiTextWatcher(
                            stepName,
                            linear_container,
                            edttext
                        )
                    )
                    main_container.addView(edttext)
                    if (!TextUtils.isEmpty(jsonObject.optString("default"))) {
                        val tdval = jsonObject.optString("default").toString().replace("\$n", "\n")
                        edttext.setText(tdval)
                    }
                }
            }
        } else {
            counter++
            val edttext = EditText(context)
            edttext.isFocusableInTouchMode = true
            edttext.layoutParams = params
            edttext.isVerticalScrollBarEnabled = true
            context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                edttext.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it
                )
            }
            try {
                edttext.id = ViewUtil.generateViewId()
                if (jsonObject?.has("disabled") == true) {
                    edttext.isEnabled = false
                    edttext.setTextColor(Color.BLACK)
                } else {
                    edttext.setLines(4)
                }
                edttext.gravity = Gravity.TOP
                edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttext.hint = jsonObject.getString("placeholder")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            edttext.setOnTouchListener(touchListener)
            DynamicElementCreator(stepName, linear_container, edttext)
            edttext.addTextChangedListener(MultiTextWatcher(stepName, linear_container, edttext))
            main_container.addView(edttext)
            if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
                val tdval = jsonObject?.optString("default").toString().replace("\$n", "\n")
                edttext.setText(tdval)
            }
        }
        add_btn.setOnClickListener {
            counter++
            val edttextitemInner = EditText(context)
            edttextitemInner.isFocusableInTouchMode = true
            edttextitemInner.layoutParams = params
            edttextitemInner.isVerticalScrollBarEnabled = true
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let { it1 ->
                edttextitemInner.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it1
                )
            }
            try {
                edttextitemInner.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttextitemInner.hint = jsonObject.getString("placeholder")
                }
                if (jsonObject?.has("disabled") == true) {
                    //  edttextitemInner.setEnabled(false);
                } else {
                    edttextitemInner.setLines(4)
                }
                edttextitemInner.gravity = Gravity.TOP
                edttextitemInner.setTag(R.id.type, jsonObject?.getString("type"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            edttextitemInner.setOnTouchListener(touchListener)
            DynamicElementCreator(stepName, linear_container, edttextitemInner)
            edttextitemInner.addTextChangedListener(
                MultiTextWatcher(
                    stepName,
                    linear_container,
                    edttextitemInner
                )
            )
            main_container.addView(edttextitemInner)
        }
        views.add(linear_container)
        return views
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        counter = 0
        val linear_container =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                linear_container.setTag(R.id.v_required, requiredValue)
                linear_container.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
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
            if (jsonObject.has("mi")) {
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
                        edttextitem.layoutParams = params
                        edttextitem.isVerticalScrollBarEnabled = true
                        context?.resources?.getColor(R.color.grey)
                            ?.let { edttextitem.setHintTextColor(it) }
                        context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                            edttextitem.setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                it
                            )
                        }
                        try {
                            edttextitem.setTag(R.id.key, jsonArray.getJSONObject(x).getString("id"))
                            if (jsonObject.has("disabled")) {
                                edttextitem.isEnabled = false
                                edttextitem.setTextColor(Color.BLACK)
                            } else {
                                edttextitem.setLines(4)
                            }
                            edttextitem.gravity = Gravity.TOP
                            if (jsonObject.has("placeholder") == true) {
                                edttextitem.hint = jsonObject.getString("placeholder")
                            }
                            if (jsonArray.getJSONObject(x).has("defaultValue")) {
                                val tdval =
                                    jsonArray.getJSONObject(x).getString("defaultValue").toString()
                                        .replace("\$n", "\n")
                                edttextitem.setText(tdval)
                            }
                            if (jsonArray.getJSONObject(x).has("val")) {
                                val tdval = jsonArray.getJSONObject(x).getString("val").toString()
                                    .replace("\$n", "\n")
                                edttextitem.setText(tdval)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        edttextitem.setOnTouchListener(touchListener)
                        edttextitem.addTextChangedListener(
                            MultiTextWatcher(
                                stepName,
                                linear_container,
                                edttextitem
                            )
                        )
                        main_container.addView(edttextitem)
                    }
                } else {
                    counter++
                    val edttext = EditText(context)
                    edttext.isFocusableInTouchMode = true
                    edttext.layoutParams = params
                    edttext.isVerticalScrollBarEnabled = true
                    context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
                    context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                        edttext.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            it
                        )
                    }
                    try {
                        edttext.id = ViewUtil.generateViewId()
                        if (jsonObject.has("disabled")) {
                            edttext.isEnabled = false
                            edttext.setTextColor(Color.BLACK)
                        } else {
                            edttext.setLines(4)
                        }
                        edttext.gravity = Gravity.TOP
                        edttext.setTag(R.id.key, jsonObject.getString("id") + "_" + counter)
                        if (jsonObject.has("placeholder")) {
                            edttext.hint = jsonObject.getString("placeholder")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    edttext.setOnTouchListener(touchListener)
                    DynamicElementCreator(stepName, linear_container, edttext)
                    edttext.addTextChangedListener(
                        MultiTextWatcher(
                            stepName,
                            linear_container,
                            edttext
                        )
                    )
                    main_container.addView(edttext)
                    if (!TextUtils.isEmpty(jsonObject.optString("default"))) {
                        val tdval = jsonObject.optString("default").toString().replace("\$n", "\n")
                        edttext.setText(tdval)
                    }
                }
            }
        } else {
            counter++
            val edttext = EditText(context)
            edttext.isFocusableInTouchMode = true
            edttext.layoutParams = params
            edttext.isVerticalScrollBarEnabled = true
            context?.resources?.getColor(R.color.grey)?.let { edttext.setHintTextColor(it) }
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let {
                edttext.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it
                )
            }
            try {
                edttext.id = ViewUtil.generateViewId()
                if (jsonObject?.has("disabled") == true) {
                    edttext.isEnabled = false
                    edttext.setTextColor(Color.BLACK)
                } else {
                    edttext.setLines(4)
                }
                edttext.gravity = Gravity.TOP
                edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttext.hint = jsonObject.getString("placeholder")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            edttext.setOnTouchListener(touchListener)
            DynamicElementCreator(stepName, linear_container, edttext)
            edttext.addTextChangedListener(MultiTextWatcher(stepName, linear_container, edttext))
            main_container.addView(edttext)
            if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
                val tdval = jsonObject?.optString("default").toString().replace("\$n", "\n")
                edttext.setText(tdval)
            }
        }
        add_btn.setOnClickListener {
            counter++
            val edttextitemInner = EditText(context)
            edttextitemInner.isFocusableInTouchMode = true
            edttextitemInner.layoutParams = params
            edttextitemInner.isVerticalScrollBarEnabled = true
            context?.resources?.getDimension(R.dimen.dynamic_form_edit_textsize)?.let { it1 ->
                edttextitemInner.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    it1
                )
            }
            try {
                edttextitemInner.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttextitemInner.hint = jsonObject.getString("placeholder")
                }
                if (jsonObject?.has("disabled") == true) {
                    //  edttextitemInner.setEnabled(false);
                } else {
                    edttextitemInner.setLines(4)
                }
                edttextitemInner.gravity = Gravity.TOP
                edttextitemInner.setTag(R.id.type, jsonObject?.getString("type"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            edttextitemInner.setOnTouchListener(touchListener)
            DynamicElementCreator(stepName, linear_container, edttextitemInner)
            edttextitemInner.addTextChangedListener(
                MultiTextWatcher(
                    stepName,
                    linear_container,
                    edttextitemInner
                )
            )
            main_container.addView(edttextitemInner)
        }
        return linear_container
    }
}