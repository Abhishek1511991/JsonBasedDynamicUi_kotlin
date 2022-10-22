package com.demo.dynamicjsonui.dynamicui.widgets


import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.custom_views.DynamicElementCreatorChanger
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.rey.material.util.ViewUtil
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Abhishek at 18/10/2022
 */
class MultiSpinnerFactory : FormWidgetFactory {
    var counter = 0

    @Throws(JSONException::class)
    fun addView(
        count: Int,
        jsonObject: JSONObject,
        context: Context?,
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
            var indexToSelect = 0
            val valuesJson = jsonObject.optJSONArray("options")
            var values: Array<String?>? = null
            if (valuesJson != null && valuesJson.length() > 0) {
                values = arrayOfNulls(valuesJson.length() + 1)
                values[0] = jsonObject.getString("title")
                for (i in 0 until valuesJson.length()) {
                    val mObj = valuesJson.getJSONObject(i)
                    val defval = mObj.getString("fieldContent")
                    values[i + 1] = mObj["displayText"].toString()
                    if (mCheck != null) {
                        if (defval.equals(mCheck, ignoreCase = true)) {
                            indexToSelect = i + 1
                            // new DynamicElementCreatorChanger(stepName, multi_container, jsonObject.getString("id"), jsonObject.getString("id") + "_" + count, mCheck, mCheck);
                        }
                    }
                }
            }
            var spinner: Spinner? = null
            if (values != null) {
                spinner = Spinner(context)
                spinner.setTag(R.id.key, jsonObject.getString("id"))
                spinner.setTag(R.id.type, jsonObject.getString("type"))
                spinner.setTag(R.id.pkey, jsonObject.getString("id") + "_" + count)
                spinner.setTag(R.id.childKey, valuesJson?.toString() ?: "")
                spinner.adapter =
                    ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, values)
                spinner.setSelection(indexToSelect, true)
                spinner.onItemSelectedListener = listener
                if (isDisabled) {
                    if (jsonObject.has("disabled")) {
                        spinner.isEnabled = false
                    }
                }
                linearLayout.addView(spinner)
            }
            multi_container.addView(linearLayout)
        }
    }

    companion object {
        fun validate(childAt: LinearLayout, focus: Boolean): ValidationStatus {
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val multi_container = childAt.findViewById<View>(R.id.multi_container) as LinearLayout
            for (x in 0 until multi_container.childCount) {
                val view = multi_container.getChildAt(x)
                if (view is LinearLayout) {
                    val mmlay = multi_container.getChildAt(x) as LinearLayout
                    for (z in 0 until mmlay.childCount) {
                        val spinner = mmlay.getChildAt(z) as Spinner
                        val selectedItemPosition = spinner.selectedItemPosition
                        if (selectedItemPosition > 0) {
                            return ValidationStatus(true, null)
                        }
                    }
                } else if (view is TextView) // update by abhishek
                {
                    // return new ValidationStatus(true, (String) childAt.getTag(R.id.error));
                    val errorMsg = childAt.getTag(R.id.error) as String
                    return if (errorMsg != null && !errorMsg.isEmpty()) ValidationStatus(
                        true,
                        childAt.getTag(R.id.error) as String
                    ) else ValidationStatus(
                        true,
                        ""
                    )
                }
            }

            // return new ValidationStatus(false, (String) childAt.getTag(R.id.error));
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

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        val views: MutableList<View> = ArrayList(1)
        counter = 0
        val checkbox_con =
            LayoutInflater.from(context).inflate(R.layout.multi_layout, null) as LinearLayout
        checkbox_con.id = ViewUtil.generateViewId()
        checkbox_con.setTag(R.id.key, jsonObject?.getString("id"))
        checkbox_con.setTag(R.id.type, jsonObject?.getString("type"))
        val multi_lable = checkbox_con.findViewById<View>(R.id.multi_lable) as TextView
        multi_lable.text = jsonObject?.getString("title")
        val multi_container = checkbox_con.findViewById<View>(R.id.multi_container) as LinearLayout
        multi_container.setTag(R.id.key, jsonObject?.getString("id"))
        multi_container.setTag(R.id.type, jsonObject?.getString("type"))
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                checkbox_con.setTag(R.id.v_required, requiredValue)
                checkbox_con.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        val add_btn = checkbox_con.findViewById<View>(R.id.add_btn) as ImageView
        if (jsonObject?.has("mi") == true) {
            val singleMultiple = jsonObject?.getString("mi")
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
                val singleMultiple = jsonObject?.getString("mi")
                if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                    add_btn.visibility = View.VISIBLE
                } else {
                    add_btn.visibility = View.GONE
                }
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
                        context,
                        listener,
                        multi_container,
                        stepName,
                        false,
                        mfield,
                        true
                    )
                }
            } else {
                counter++
                addView(
                    counter,
                    jsonObject,
                    context,
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
                        context,
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
        views.add(checkbox_con)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        TODO("Not yet implemented")
    }
}