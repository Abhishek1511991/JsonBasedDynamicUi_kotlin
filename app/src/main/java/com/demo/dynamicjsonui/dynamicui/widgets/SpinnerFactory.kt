package com.demo.dynamicjsonui.dynamicui.widgets


import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.rey.material.util.ViewUtil
import com.rey.material.widget.Spinner
import org.json.JSONObject

/**
 * Created by Abhishek at 18/10/2022
 */
class SpinnerFactory : FormWidgetFactory {
    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        val views: MutableList<View> = ArrayList(1)
        val spinner: Spinner =
            LayoutInflater.from(context).inflate(R.layout.item_spinner, null) as Spinner
        val hint = jsonObject?.optString("title")
        if (!TextUtils.isEmpty(hint)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                spinner.setAutofillHints(arrayOf(jsonObject?.getString("title")).toString())
            }
        }
        if (jsonObject?.has("disabled") == true) {
            spinner.setEnabled(false)
        }
        spinner.setId(ViewUtil.generateViewId())
        spinner.setTag(R.id.key, jsonObject?.getString("id"))
        spinner.setTag(R.id.type, jsonObject?.getString("type"))
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                spinner.setTag(R.id.v_required, requiredValue)
                spinner.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        val requiredObject = jsonObject?.optJSONObject("v_required")
        if (requiredObject != null) {
            val requiredValue = requiredObject.getString("value")
            if (!TextUtils.isEmpty(requiredValue)) {
                spinner.setTag(R.id.v_required, requiredValue)
                spinner.setTag(R.id.error, requiredObject.optString("err"))
            }
        }
        var valueToSelect: String? = ""
        var indexToSelect = -1
        if (!TextUtils.isEmpty(jsonObject?.optString("value"))) {
            valueToSelect = jsonObject?.optString("value")
        }
        val valuesJson = jsonObject?.optJSONArray("options")
        var values: Array<String?>? = null
        if (valuesJson != null && valuesJson.length() > 0) {
            values = arrayOfNulls(valuesJson.length())
            for (i in 0 until valuesJson.length()) {
                val mObj = valuesJson.getJSONObject(i)
                val defval = mObj.getString("default")
                values[i] = mObj["displayText"].toString()
                if (defval.equals("true", ignoreCase = true)) {
                    indexToSelect = i
                }
            }
        }
        if (values != null) {
            spinner.adapter=ArrayAdapter(context!!, android.R.layout.simple_list_item_1, values)
            spinner.setSelection(indexToSelect + 1)
            spinner.setOnItemSelectedListener(listener)
        }
        views.add(spinner)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        val spinner: Spinner =
            LayoutInflater.from(context).inflate(R.layout.item_spinner, null) as Spinner
        val hint = jsonObject?.optString("title")
        if (!TextUtils.isEmpty(hint)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                spinner.setAutofillHints(arrayOf(jsonObject?.getString("title")).toString())
            }
        }
        if (jsonObject?.has("disabled") == true) {
            spinner.setEnabled(false)
        }
        spinner.setId(ViewUtil.generateViewId())
        spinner.setTag(R.id.key, jsonObject?.getString("id"))
        spinner.setTag(R.id.type, jsonObject?.getString("type"))
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                spinner.setTag(R.id.v_required, requiredValue)
                spinner.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        val requiredObject = jsonObject?.optJSONObject("v_required")
        if (requiredObject != null) {
            val requiredValue = requiredObject.getString("value")
            if (!TextUtils.isEmpty(requiredValue)) {
                spinner.setTag(R.id.v_required, requiredValue)
                spinner.setTag(R.id.error, requiredObject.optString("err"))
            }
        }
        var valueToSelect: String? = ""
        var indexToSelect = -1
        if (!TextUtils.isEmpty(jsonObject?.optString("value"))) {
            valueToSelect = jsonObject?.optString("value")
        }
        val valuesJson = jsonObject?.optJSONArray("options")
        var values: Array<String?>? = null
        if (valuesJson != null && valuesJson.length() > 0) {
            values = arrayOfNulls(valuesJson.length())
            for (i in 0 until valuesJson.length()) {
                val mObj = valuesJson.getJSONObject(i)
                val defval = mObj.getString("default")
                values[i] = mObj["displayText"].toString()
                if (defval.equals("true", ignoreCase = true)) {
                    indexToSelect = i
                }
            }
        }
        if (values != null) {
            spinner.adapter=ArrayAdapter(context!!, android.R.layout.simple_list_item_1, values)
            spinner.setSelection(indexToSelect + 1)
            spinner.setOnItemSelectedListener(listener)
        }
        return spinner
    }
}