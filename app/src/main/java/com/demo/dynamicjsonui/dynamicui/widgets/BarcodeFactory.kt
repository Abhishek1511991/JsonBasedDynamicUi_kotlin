package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.activity.Barcode_Activity
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.custom_views.DynamicElementCreator
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.rey.material.util.ViewUtil
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Abhishek at 20/10/2022
 */
class BarcodeFactory : FormWidgetFactory {
    var counter = 0


    companion object {
        var validator = ArrayList<JSONObject>()
        fun validate(childAt: LinearLayout): ValidationStatus {
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val multi_container = childAt.findViewById<View>(R.id.barcode_container) as LinearLayout
            val count = multi_container.childCount
            for (i in 0 until count) {
                if ((multi_container.getChildAt(i)
                        .findViewById<View>(R.id.barcode_txt) as TextView).text.toString().isEmpty()
                ) {

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
    ): List<View?>{
        counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container = LayoutInflater.from(context)
            .inflate(R.layout.barcode_layout_factory, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val main_container =
            linear_container.findViewById<View>(R.id.barcode_container) as LinearLayout
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

        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val jsonArray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (jsonArray.length() > 0) {
                for (x in 0 until jsonArray.length()) {
                    counter++
                    val barcodelay = LayoutInflater.from(context)
                        .inflate(R.layout.barcode_raw_lay, null) as LinearLayout
                    val barcode_txt =
                        barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
                    val barcode_img =
                        barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
                    if (jsonObject.has("disabled")) {
                        barcode_img.visibility = View.GONE
                    }
                    if (jsonArray.getJSONObject(x).has("val")) {
                        barcode_txt.text = jsonArray.getJSONObject(x)["val"].toString()
                        barcode_txt.setTextColor(Color.BLACK)
                    }
                    try {
                        val pid_key = jsonObject.getString("id")
                        val id_key = jsonObject.getString("id") + "_" + counter
                        barcodelay.setTag(R.id.key, id_key)
                        barcode_img.setOnClickListener {
                            val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                            barcodeIntent.putExtra("pkey", pid_key)
                            barcodeIntent.putExtra("ckey", id_key)
                            barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            context?.startActivity(barcodeIntent)
                        }

                        //  new DynamicElementCreator(stepName, barcodelay, pid_key, id_key, "");
                        main_container.addView(barcodelay)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } else {
                counter++
                val barcodelay = LayoutInflater.from(context)
                    .inflate(R.layout.barcode_raw_lay, null) as LinearLayout
                val barcode_txt = barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
                val barcode_img = barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
                try {
                    val pid_key = jsonObject.getString("id")
                    val id_key = jsonObject.getString("id") + "_" + counter
                    if (jsonObject.has("disabled")) {
                        barcode_img.visibility = View.GONE
                    }
                    barcodelay.setTag(R.id.key, id_key)
                    barcode_txt.setTextColor(Color.BLACK)
                    barcode_img.setOnClickListener {
                        val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                        barcodeIntent.putExtra("pkey", pid_key)
                        barcodeIntent.putExtra("ckey", id_key)
                        barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        context?.startActivity(barcodeIntent)
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("disabled"))) {
                        barcodelay.isEnabled = false
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("default"))) {
                        barcode_txt.text = jsonObject.optString("default")
                    }
                    DynamicElementCreator(stepName!!, barcodelay, pid_key, id_key, "")
                    main_container.addView(barcodelay)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            counter++
            val barcodelay =
                LayoutInflater.from(context).inflate(R.layout.barcode_raw_lay, null) as LinearLayout
            val barcode_txt = barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
            val barcode_img = barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
            try {
                val pid_key = jsonObject?.getString("id")
                val id_key = jsonObject?.getString("id") + "_" + counter
                if (jsonObject?.has("disabled") == true) {
                    barcode_img.visibility = View.GONE
                }
                barcodelay.setTag(R.id.key, id_key)
                barcode_img.setOnClickListener {
                    val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                    barcodeIntent.putExtra("pkey", pid_key)
                    barcodeIntent.putExtra("ckey", id_key)
                    barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context?.startActivity(barcodeIntent)
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                    barcodelay.isEnabled = false
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
                    barcode_txt.text = jsonObject?.optString("default")
                    barcode_txt.setTextColor(Color.BLACK)
                }
                DynamicElementCreator(stepName!!, barcodelay, pid_key, id_key, "")
                main_container.addView(barcodelay)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        add_btn.setOnClickListener {
            counter++
            val barcodelay =
                LayoutInflater.from(context).inflate(R.layout.barcode_raw_lay, null) as LinearLayout
            val barcode_txt = barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
            val barcode_img = barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
            barcode_txt.setTextColor(Color.BLACK)
            try {
                val pid_key = jsonObject?.getString("id")
                val id_key = jsonObject?.getString("id") + "_" + counter
                if (jsonObject?.has("disabled") == true) {
                    //  barcode_img.setVisibility(View.GONE);
                }
                barcodelay.setTag(R.id.key, id_key)
                barcode_img.setOnClickListener {
                    val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                    barcodeIntent.putExtra("pkey", pid_key)
                    barcodeIntent.putExtra("ckey", id_key)
                    barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context?.startActivity(barcodeIntent)
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                    barcodelay.isEnabled = false
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
                    barcode_txt.text = jsonObject?.optString("default")
                }
                DynamicElementCreator(stepName!!, barcodelay, pid_key, id_key, "")
                main_container.addView(barcodelay)
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
    ): View {
        counter = 0
        val linear_container = LayoutInflater.from(context)
            .inflate(R.layout.barcode_layout_factory, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val main_container =
            linear_container.findViewById<View>(R.id.barcode_container) as LinearLayout
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

        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val jsonArray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (jsonArray.length() > 0) {
                for (x in 0 until jsonArray.length()) {
                    counter++
                    val barcodelay = LayoutInflater.from(context)
                        .inflate(R.layout.barcode_raw_lay, null) as LinearLayout
                    val barcode_txt =
                        barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
                    val barcode_img =
                        barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
                    if (jsonObject.has("disabled")) {
                        barcode_img.visibility = View.GONE
                    }
                    if (jsonArray.getJSONObject(x).has("val")) {
                        barcode_txt.text = jsonArray.getJSONObject(x)["val"].toString()
                        barcode_txt.setTextColor(Color.BLACK)
                    }
                    try {
                        val pid_key = jsonObject.getString("id")
                        val id_key = jsonObject.getString("id") + "_" + counter
                        barcodelay.setTag(R.id.key, id_key)
                        barcode_img.setOnClickListener {
                            val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                            barcodeIntent.putExtra("pkey", pid_key)
                            barcodeIntent.putExtra("ckey", id_key)
                            barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            context?.startActivity(barcodeIntent)
                        }

                        //  new DynamicElementCreator(stepName, barcodelay, pid_key, id_key, "");
                        main_container.addView(barcodelay)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } else {
                counter++
                val barcodelay = LayoutInflater.from(context)
                    .inflate(R.layout.barcode_raw_lay, null) as LinearLayout
                val barcode_txt = barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
                val barcode_img = barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
                try {
                    val pid_key = jsonObject.getString("id")
                    val id_key = jsonObject.getString("id") + "_" + counter
                    if (jsonObject.has("disabled")) {
                        barcode_img.visibility = View.GONE
                    }
                    barcodelay.setTag(R.id.key, id_key)
                    barcode_txt.setTextColor(Color.BLACK)
                    barcode_img.setOnClickListener {
                        val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                        barcodeIntent.putExtra("pkey", pid_key)
                        barcodeIntent.putExtra("ckey", id_key)
                        barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        context?.startActivity(barcodeIntent)
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("disabled"))) {
                        barcodelay.isEnabled = false
                    }
                    if (!TextUtils.isEmpty(jsonObject.optString("default"))) {
                        barcode_txt.text = jsonObject.optString("default")
                    }
                    DynamicElementCreator(stepName!!, barcodelay, pid_key, id_key, "")
                    main_container.addView(barcodelay)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            counter++
            val barcodelay =
                LayoutInflater.from(context).inflate(R.layout.barcode_raw_lay, null) as LinearLayout
            val barcode_txt = barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
            val barcode_img = barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
            try {
                val pid_key = jsonObject?.getString("id")
                val id_key = jsonObject?.getString("id") + "_" + counter
                if (jsonObject?.has("disabled") == true) {
                    barcode_img.visibility = View.GONE
                }
                barcodelay.setTag(R.id.key, id_key)
                barcode_img.setOnClickListener {
                    val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                    barcodeIntent.putExtra("pkey", pid_key)
                    barcodeIntent.putExtra("ckey", id_key)
                    barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context?.startActivity(barcodeIntent)
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                    barcodelay.isEnabled = false
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
                    barcode_txt.text = jsonObject?.optString("default")
                    barcode_txt.setTextColor(Color.BLACK)
                }
                DynamicElementCreator(stepName!!, barcodelay, pid_key, id_key, "")
                main_container.addView(barcodelay)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        add_btn.setOnClickListener {
            counter++
            val barcodelay =
                LayoutInflater.from(context).inflate(R.layout.barcode_raw_lay, null) as LinearLayout
            val barcode_txt = barcodelay.findViewById<View>(R.id.barcode_txt) as TextView
            val barcode_img = barcodelay.findViewById<View>(R.id.barcode_btn) as ImageView
            barcode_txt.setTextColor(Color.BLACK)
            try {
                val pid_key = jsonObject?.getString("id")
                val id_key = jsonObject?.getString("id") + "_" + counter
                if (jsonObject?.has("disabled") == true) {
                    //  barcode_img.setVisibility(View.GONE);
                }
                barcodelay.setTag(R.id.key, id_key)
                barcode_img.setOnClickListener {
                    val barcodeIntent = Intent(context, Barcode_Activity::class.java)
                    barcodeIntent.putExtra("pkey", pid_key)
                    barcodeIntent.putExtra("ckey", id_key)
                    barcodeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    context?.startActivity(barcodeIntent)
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("disabled"))) {
                    barcodelay.isEnabled = false
                }
                if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
                    barcode_txt.text = jsonObject?.optString("default")
                }
                DynamicElementCreator(stepName!!, barcodelay, pid_key, id_key, "")
                main_container.addView(barcodelay)
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