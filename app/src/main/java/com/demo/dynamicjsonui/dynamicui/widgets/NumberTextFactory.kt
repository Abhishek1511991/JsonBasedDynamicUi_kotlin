package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
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
class NumberTextFactory : FormWidgetFactory {
    var counter = 0
    var step = 1
    var min = 0
    var max = 100


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
        step = 1
        min = 0
        max = 100
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
            if (jsonObject?.has("mi") == true) {
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
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER_HORIZONTAL
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val jsonArray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    for (x in 0 until jsonArray.length()) {
                        counter++
                        val seekBar = SeekBar(context)
                        if (jsonObject.has("min") && jsonObject.has("max")) {
                            max = jsonObject.getInt("max")
                            min = jsonObject.getInt("min")
                            seekBar.setMax((max - min) / step)
                        } else {
                            seekBar.setMax(max)
                        }
                        val edttextitem = EditText(context)
                        edttextitem.isFocusableInTouchMode = true
                        edttextitem.setTextColor(Color.BLACK)
                        edttextitem.isEnabled = false
                        edttextitem.setTextColor(Color.BLACK)
                        if (jsonObject.has("disabled")) {
                            seekBar.setEnabled(false)
                        }
                        edttextitem.inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        edttextitem.layoutParams = params
                        try {
                            edttextitem.setTag(R.id.key, jsonArray.getJSONObject(x).getString("id"))
                            if (jsonObject.has("placeholder")) {
                                edttextitem.hint = jsonObject.getString("placeholder")
                            }
                            if (jsonArray.getJSONObject(x).has("defaultValue")) {
                                edttextitem.setText(
                                    jsonArray.getJSONObject(x).getString("defaultValue")
                                )
                                seekBar.setProgress(
                                    Integer.valueOf(
                                        jsonArray.getJSONObject(x).getString("defaultValue")
                                    )
                                )
                            }
                            if (jsonArray.getJSONObject(x).has("val")) {
                                edttextitem.setText(jsonArray.getJSONObject(x).getString("val"))
                                seekBar.setProgress(
                                    Integer.valueOf(
                                        jsonArray.getJSONObject(x).getString("val")
                                    )
                                )
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
                        seekBar.setOnSeekBarChangeListener(object :
                            SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar,
                                progress: Int,
                                b: Boolean
                            ) {
                                if (b) {
                                    var value = 0
                                    try {
                                        value = jsonObject.getInt("min") + progress * step
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    edttextitem.setText(value.toString())
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar) {}
                        })
                        main_container.addView(seekBar)
                        main_container.addView(edttextitem)
                    }
                } else {
                    counter++
                    val seekBar = SeekBar(context)
                    if (jsonObject.has("min") && jsonObject.has("max")) {
                        max = jsonObject.getInt("max")
                        min = jsonObject.getInt("min")
                        seekBar.setMax((max - min) / step)
                    } else {
                        seekBar.setMax(max)
                    }
                    val edttext = EditText(context)
                    edttext.isFocusableInTouchMode = true
                    edttext.isEnabled = false
                    if (jsonObject.has("disabled")) {
                        seekBar.setEnabled(false)
                    }
                    edttext.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    edttext.layoutParams = params
                    try {
                        edttext.id = ViewUtil.generateViewId()
                        edttext.setTextColor(Color.BLACK)
                        edttext.setTag(R.id.key, jsonObject.getString("id") + "_" + counter)
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
                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar,
                            progress: Int,
                            b: Boolean
                        ) {
                            if (b) {
                                var value = 0
                                try {
                                    value = jsonObject.getInt("min") + progress * step
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                edttext.setText(value.toString())
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar) {}
                    })
                    main_container.addView(seekBar)
                    main_container.addView(edttext)
                    if (!TextUtils.isEmpty(jsonObject.optString("disabled"))) {
                        edttext.isEnabled = false
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("defaultValue"))) {
                        edttext.setText(jsonObject.optString("defaultValue"))
                        seekBar.setProgress(Integer.valueOf(jsonObject.optString("defaultValue")))
                    }
                }
            }
        } else {
            counter++
            val seekBar = SeekBar(context)
            if (jsonObject?.has("min") == true && jsonObject.has("max")) {
                max = jsonObject.getInt("max")
                min = jsonObject.getInt("min")
                seekBar.setMax((max - min) / step)
            } else {
                seekBar.setMax(max)
            }
            val edttext = EditText(context)
            edttext.isFocusableInTouchMode = true
            edttext.setTextColor(Color.BLACK)
            edttext.isEnabled = false
            if (jsonObject?.has("disabled") == true) {
                seekBar.setEnabled(false)
            }
            edttext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            edttext.layoutParams = params
            try {
                edttext.id = ViewUtil.generateViewId()
                edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttext.hint = jsonObject.getString("placeholder")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            DynamicElementCreator(stepName, linear_container, edttext)
            edttext.addTextChangedListener(MultiTextWatcher(stepName, linear_container, edttext))
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                    if (b) {
                        var value = 0
                        try {
                            value = jsonObject?.getInt("min")?.plus(progress * step) ?: 0
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        edttext.setText(value.toString())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
            main_container.addView(seekBar)
            main_container.addView(edttext)
            if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                edttext.isEnabled = false
            }
            if (!TextUtils.isEmpty(jsonObject?.optString("defaultValue"))) {
                edttext.setText(jsonObject?.optString("defaultValue"))
                jsonObject?.optString("defaultValue")
                    ?.let { Integer.valueOf(it) }?.let { seekBar.setProgress(it) }
            }
        }
        add_btn.setOnClickListener {
            counter++
            val edttextitemInner = EditText(context)
            edttextitemInner.isFocusableInTouchMode = true
            edttextitemInner.isEnabled = false
            edttextitemInner.setTextColor(Color.BLACK)
            if (jsonObject?.has("disabled") == true) {
                //
            }
            edttextitemInner.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            edttextitemInner.layoutParams = params
            try {
                val seekBar = SeekBar(context)
                if (jsonObject?.has("min") == true && jsonObject.has("max") == true) {
                    max = jsonObject.getInt("max")
                    min = jsonObject.getInt("min")
                    seekBar.setMax((max - min) / step)
                } else {
                    seekBar.setMax(max)
                }
                edttextitemInner.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttextitemInner.hint = jsonObject.getString("placeholder")
                }
                if (jsonObject?.has("type") == true) {
                    edttextitemInner.setTag(R.id.type, jsonObject.getString("type"))
                }
                DynamicElementCreator(stepName, linear_container, edttextitemInner)
                edttextitemInner.addTextChangedListener(
                    MultiTextWatcher(
                        stepName,
                        linear_container,
                        edttextitemInner
                    )
                )
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        if (b) {
                            var value = 0
                            try {
                                value = jsonObject?.getInt("min")?.plus(progress * step) ?: 0
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            edttextitemInner.setText(value.toString())
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                })
                main_container.addView(seekBar)
                main_container.addView(edttextitemInner)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
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
    ): View
    {
        counter = 0
        step = 1
        min = 0
        max = 100
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
            if (jsonObject?.has("mi") == true) {
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
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER_HORIZONTAL
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val jsonArray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    for (x in 0 until jsonArray.length()) {
                        counter++
                        val seekBar = SeekBar(context)
                        if (jsonObject.has("min") && jsonObject.has("max")) {
                            max = jsonObject.getInt("max")
                            min = jsonObject.getInt("min")
                            seekBar.setMax((max - min) / step)
                        } else {
                            seekBar.setMax(max)
                        }
                        val edttextitem = EditText(context)
                        edttextitem.isFocusableInTouchMode = true
                        edttextitem.setTextColor(Color.BLACK)
                        edttextitem.isEnabled = false
                        edttextitem.setTextColor(Color.BLACK)
                        if (jsonObject.has("disabled")) {
                            seekBar.setEnabled(false)
                        }
                        edttextitem.inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        edttextitem.layoutParams = params
                        try {
                            edttextitem.setTag(R.id.key, jsonArray.getJSONObject(x).getString("id"))
                            if (jsonObject.has("placeholder")) {
                                edttextitem.hint = jsonObject.getString("placeholder")
                            }
                            if (jsonArray.getJSONObject(x).has("defaultValue")) {
                                edttextitem.setText(
                                    jsonArray.getJSONObject(x).getString("defaultValue")
                                )
                                seekBar.setProgress(
                                    Integer.valueOf(
                                        jsonArray.getJSONObject(x).getString("defaultValue")
                                    )
                                )
                            }
                            if (jsonArray.getJSONObject(x).has("val")) {
                                edttextitem.setText(jsonArray.getJSONObject(x).getString("val"))
                                seekBar.setProgress(
                                    Integer.valueOf(
                                        jsonArray.getJSONObject(x).getString("val")
                                    )
                                )
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
                        seekBar.setOnSeekBarChangeListener(object :
                            SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar,
                                progress: Int,
                                b: Boolean
                            ) {
                                if (b) {
                                    var value = 0
                                    try {
                                        value = jsonObject.getInt("min") + progress * step
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    edttextitem.setText(value.toString())
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar) {}
                        })
                        main_container.addView(seekBar)
                        main_container.addView(edttextitem)
                    }
                } else {
                    counter++
                    val seekBar = SeekBar(context)
                    if (jsonObject.has("min") && jsonObject.has("max")) {
                        max = jsonObject.getInt("max")
                        min = jsonObject.getInt("min")
                        seekBar.setMax((max - min) / step)
                    } else {
                        seekBar.setMax(max)
                    }
                    val edttext = EditText(context)
                    edttext.isFocusableInTouchMode = true
                    edttext.isEnabled = false
                    if (jsonObject.has("disabled")) {
                        seekBar.setEnabled(false)
                    }
                    edttext.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    edttext.layoutParams = params
                    try {
                        edttext.id = ViewUtil.generateViewId()
                        edttext.setTextColor(Color.BLACK)
                        edttext.setTag(R.id.key, jsonObject.getString("id") + "_" + counter)
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
                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar,
                            progress: Int,
                            b: Boolean
                        ) {
                            if (b) {
                                var value = 0
                                try {
                                    value = jsonObject.getInt("min") + progress * step
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                edttext.setText(value.toString())
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar) {}
                    })
                    main_container.addView(seekBar)
                    main_container.addView(edttext)
                    if (!TextUtils.isEmpty(jsonObject.optString("disabled"))) {
                        edttext.isEnabled = false
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("defaultValue"))) {
                        edttext.setText(jsonObject.optString("defaultValue"))
                        seekBar.setProgress(Integer.valueOf(jsonObject.optString("defaultValue")))
                    }
                }
            }
        } else {
            counter++
            val seekBar = SeekBar(context)
            if (jsonObject?.has("min") == true && jsonObject.has("max")) {
                max = jsonObject.getInt("max")
                min = jsonObject.getInt("min")
                seekBar.setMax((max - min) / step)
            } else {
                seekBar.setMax(max)
            }
            val edttext = EditText(context)
            edttext.isFocusableInTouchMode = true
            edttext.setTextColor(Color.BLACK)
            edttext.isEnabled = false
            if (jsonObject?.has("disabled") == true) {
                seekBar.setEnabled(false)
            }
            edttext.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            edttext.layoutParams = params
            try {
                edttext.id = ViewUtil.generateViewId()
                edttext.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttext.hint = jsonObject.getString("placeholder")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            DynamicElementCreator(stepName, linear_container, edttext)
            edttext.addTextChangedListener(MultiTextWatcher(stepName, linear_container, edttext))
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                    if (b) {
                        var value = 0
                        try {
                            value = jsonObject?.getInt("min")?.plus(progress * step) ?:0
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        edttext.setText(value.toString())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
            main_container.addView(seekBar)
            main_container.addView(edttext)
            if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                edttext.isEnabled = false
            }
            if (!TextUtils.isEmpty(jsonObject?.optString("defaultValue"))) {
                edttext.setText(jsonObject?.optString("defaultValue"))
                jsonObject?.optString("defaultValue")
                    ?.let { Integer.valueOf(it) }?.let { seekBar.setProgress(it) }
            }
        }
        add_btn.setOnClickListener {
            counter++
            val edttextitemInner = EditText(context)
            edttextitemInner.isFocusableInTouchMode = true
            edttextitemInner.isEnabled = false
            edttextitemInner.setTextColor(Color.BLACK)
            if (jsonObject?.has("disabled") == true) {
                //
            }
            edttextitemInner.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            edttextitemInner.layoutParams = params
            try {
                val seekBar = SeekBar(context)
                if (jsonObject?.has("min") == true && jsonObject.has("max")) {
                    max = jsonObject.getInt("max")
                    min = jsonObject.getInt("min")
                    seekBar.setMax((max - min) / step)
                } else {
                    seekBar.setMax(max)
                }
                edttextitemInner.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
                if (jsonObject?.has("placeholder") == true) {
                    edttextitemInner.hint = jsonObject.getString("placeholder")
                }
                if (jsonObject?.has("type") == true) {
                    edttextitemInner.setTag(R.id.type, jsonObject.getString("type"))
                }
                DynamicElementCreator(stepName, linear_container, edttextitemInner)
                edttextitemInner.addTextChangedListener(
                    MultiTextWatcher(
                        stepName,
                        linear_container,
                        edttextitemInner
                    )
                )
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        if (b) {
                            var value = 0
                            try {
                                value = jsonObject?.getInt("min")?.plus(progress * step) ?: 0
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            edttextitemInner.setText(value.toString())
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                })
                main_container.addView(seekBar)
                main_container.addView(edttextitemInner)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
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