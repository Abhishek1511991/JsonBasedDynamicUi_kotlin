package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.custom_views.DynamicElementCreatorChanger
import com.demo.dynamicjsonui.dynamicui.custom_views.RadioButton
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.FormUtils
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.rey.material.util.ViewUtil
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Abhishek at 20/10/2022
 */
class MultiRadioButtonFactory : FormWidgetFactory {
    var counter = 0


    @Throws(JSONException::class)
    fun addView(
        count: Int,
        jsonObject: JSONObject,
        context: Context,
        listener: CommonListener?,
        multi_container: LinearLayout,
        stepName: String?,
        iscreate: Boolean,
        mCheck: String?,
        isDisabled: Boolean
    ) {
        if (jsonObject.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
            val options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            val linearLayout = LinearLayout(context)
            linearLayout.setTag(R.id.key, jsonObject.getString("id") + "_" + count)
            linearLayout.setTag(R.id.type, jsonObject.getString("type"))
            linearLayout.orientation = LinearLayout.VERTICAL
            val LLParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayout.layoutParams = LLParams
            if (iscreate) {
                DynamicElementCreatorChanger(stepName, multi_container, linearLayout)
            }
            for (i in 0 until options.length()) {
                val item = options.getJSONObject(i)
                val radioButton: RadioButton = LayoutInflater.from(context)
                    .inflate(R.layout.item_radiobutton, null) as RadioButton
                if (isDisabled) {
                    if (jsonObject.has("disabled")) {
                        radioButton.setEnabled(false)
                    }
                }
                radioButton.setText(item.getString("displayText"))
                radioButton.setTag(R.id.key, jsonObject.getString("id"))
                radioButton.setTag(R.id.type, jsonObject.getString("type"))
                radioButton.setTag(R.id.pkey, jsonObject.getString("id") + "_" + count)
                radioButton.setTag(R.id.childKey, item.getString("fieldContent"))
                radioButton.setGravity(Gravity.CENTER_VERTICAL)
                radioButton.setTextColor(Color.BLACK)
                radioButton.setTextSize(16F)
                var ischecked = false
                if (!TextUtils.isEmpty(item.optString("default"))) {
                    if (item.optString("default").equals("true", ignoreCase = true)) {
                        radioButton.setChecked(true)
                        ischecked = true
                    }
                }
                if (mCheck != null) {
                    if (item.getString("fieldContent").equals(mCheck, ignoreCase = true)) {
                        radioButton.setChecked(true)

                        //   new DynamicElementCreatorChanger(stepName, multi_container, jsonObject.getString("id"), jsonObject.getString("id") + "_" + count, mCheck, mCheck);
                    }
                }
                radioButton.setOnCheckedChangeListener(listener)
                if (i == options.length() - 1) {
                    radioButton.setLayoutParams(
                        FormUtils.getLayoutParams(
                            FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, context
                                .resources.getDimension(R.dimen.extra_bottom_margin).toInt()
                        )
                    )
                }
                 linearLayout.addView(radioButton)
            }
            multi_container.addView(linearLayout)
        }
    }

    companion object {
        fun validate(childAt: LinearLayout, focus: Boolean): ValidationStatus {
            var isvalidate = true
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val multi_container =
                childAt.findViewById<View>(R.id.multi_container_sec) as LinearLayout
            val count = multi_container.childCount
            for (i in 0 until count) {
                var isch = false
                val mmlay = multi_container.getChildAt(i) as LinearLayout
                for (z in 0 until mmlay.childCount) {
                    if ((mmlay.getChildAt(z) as RadioButton).isChecked()) {
                        isvalidate = false
                        isch = true
                    }
                }
                if (!isch) {
                    isvalidate = true
                    break
                }
            }
            if (focus) {
                if (count > 0) {
                    if (isvalidate) {
                        (multi_container.getChildAt(0) as RadioButton).requestFocus()
                    }
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

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?>{
        val views: MutableList<View> = ArrayList(1)
        counter = 0
        val radiobutton_con =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_sec, null) as LinearLayout
        radiobutton_con.id = ViewUtil.generateViewId()
        radiobutton_con.setTag(R.id.key, jsonObject?.getString("id"))
        radiobutton_con.setTag(R.id.type, jsonObject?.getString("type"))
        val add_btn = radiobutton_con.findViewById<View>(R.id.add_btn) as ImageView
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
        val multi_lable = radiobutton_con.findViewById<View>(R.id.multi_lable) as TextView
        multi_lable.text = jsonObject?.getString("title")
        val multi_container =
            radiobutton_con.findViewById<View>(R.id.multi_container_sec) as LinearLayout
        multi_container.setTag(R.id.key, jsonObject?.getString("id"))
        multi_container.setTag(R.id.type, jsonObject?.getString("type"))
        multi_container.setTag(R.id.radioTag, jsonObject?.getString("id") + R.id.radioTag)
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                radiobutton_con.setTag(R.id.v_required, requiredValue)
                radiobutton_con.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }

        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            if (jsonObject.has(JsonFormConstants.OPTIONS_FIELD_CHECK)) {
                val fList = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_CHECK)
                for (x in 0 until fList.length()) {
                    val lObj = fList[x] as JSONObject
                    val mfield = lObj["val"] as String
                    counter++
                    addView(
                        counter,
                        jsonObject,
                        context!!,
                        listener,
                        multi_container,
                        stepName,
                        false,
                        mfield,
                        true
                    )
                }
            }
            else {
                counter++
                addView(
                    counter,
                    jsonObject,
                    context!!,
                    listener,
                    multi_container,
                    stepName,
                    true,
                    null,
                    true
                )
            }
            add_btn.setOnClickListener {
                counter++
                try {
                    addView(
                        counter,
                        jsonObject,
                        context!!,
                        listener,
                        multi_container,
                        stepName,
                        true,
                        null,
                        false
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        views.add(radiobutton_con)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        counter = 0
        val radiobutton_con =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_sec, null) as LinearLayout
        radiobutton_con.id = ViewUtil.generateViewId()
        radiobutton_con.setTag(R.id.key, jsonObject?.getString("id"))
        radiobutton_con.setTag(R.id.type, jsonObject?.getString("type"))
        val add_btn = radiobutton_con.findViewById<View>(R.id.add_btn) as ImageView
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
        val multi_lable = radiobutton_con.findViewById<View>(R.id.multi_lable) as TextView
        multi_lable.text = jsonObject?.getString("title")
        val multi_container =
            radiobutton_con.findViewById<View>(R.id.multi_container_sec) as LinearLayout
        multi_container.setTag(R.id.key, jsonObject?.getString("id"))
        multi_container.setTag(R.id.type, jsonObject?.getString("type"))
        multi_container.setTag(R.id.radioTag, jsonObject?.getString("id") + R.id.radioTag)
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                radiobutton_con.setTag(R.id.v_required, requiredValue)
                radiobutton_con.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }

        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            if (jsonObject.has(JsonFormConstants.OPTIONS_FIELD_CHECK)) {
                val fList = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_CHECK)
                for (x in 0 until fList.length()) {
                    val lObj = fList[x] as JSONObject
                    val mfield = lObj["val"] as String
                    counter++
                    addView(
                        counter,
                        jsonObject,
                        context!!,
                        listener,
                        multi_container,
                        stepName,
                        false,
                        mfield,
                        true
                    )
                }
            }
            else {
                counter++
                addView(
                    counter,
                    jsonObject,
                    context!!,
                    listener,
                    multi_container,
                    stepName,
                    true,
                    null,
                    true
                )
            }
            add_btn.setOnClickListener {
                counter++
                try {
                    addView(
                        counter,
                        jsonObject,
                        context!!,
                        listener,
                        multi_container,
                        stepName,
                        true,
                        null,
                        false
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        return radiobutton_con
    }
}