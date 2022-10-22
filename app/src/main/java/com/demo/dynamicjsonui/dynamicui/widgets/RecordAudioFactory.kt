package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.rey.material.util.ViewUtil
import org.json.JSONObject

/**
 * Created by Abhishek at 20/10/2022
 */
class RecordAudioFactory : FormWidgetFactory {


    companion object {
        var validator = ArrayList<JSONObject>()
        fun validate(childAt: LinearLayout, focus: Boolean): ValidationStatus {
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val multi_container = childAt.findViewById<View>(R.id.media_container) as LinearLayout
            val media_recyclerview_list: RecyclerView =
                multi_container.findViewById<View>(R.id.media_listing) as RecyclerView
            val count: Int = media_recyclerview_list.getChildCount()
            if (count <= 0) {
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
        var counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val main_container =
            linear_container.findViewById<View>(R.id.media_container) as LinearLayout
        val add_btn = linear_container.findViewById<View>(R.id.add_btn) as ImageView
        val add_signature = linear_container.findViewById<View>(R.id.add_signature) as ImageView
        add_btn.setTag(R.id.key, jsonObject?.getString("id"))
        add_btn.setTag(R.id.type, jsonObject?.getString("type"))
        add_signature.setTag(R.id.key, jsonObject?.getString("id"))
        add_signature.setTag(R.id.type, jsonObject?.getString("type"))
        if (jsonObject?.has("mi") == true) {
            val singleMultiple = jsonObject.getString("mi")
            if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                add_btn.visibility = View.VISIBLE
                add_signature.visibility = View.GONE
            } else {
                add_btn.visibility = View.GONE
                add_signature.visibility = View.VISIBLE
            }
        } else {
            add_btn.visibility = View.GONE
            add_signature.visibility = View.VISIBLE
        }
        if (jsonObject?.has("disabled") == true) {
            add_signature.isEnabled = false
            add_signature.isClickable = false
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
        val media_recyclerview_list: RecyclerView =
            main_container.findViewById<View>(R.id.media_listing) as RecyclerView
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        media_recyclerview_list.setLayoutManager(lm)
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                linear_container.setTag(R.id.v_required, requiredValue)
                linear_container.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        counter++
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val newObjarray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (newObjarray.length() > 0) {
                val mediaObj = newObjarray.getJSONObject(0)
                if (mediaObj.has("mediaId")) {
                    media_recyclerview_list.setTag(R.id.mediaId, mediaObj.getString("mediaId"))
                }
            }
        }
        //  if (!jsonObject.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
        val id_key = jsonObject?.getString("id") + "_" + counter
        media_recyclerview_list.setTag(R.id.key, id_key)
        if (jsonObject?.has("editable") == true) {
            media_recyclerview_list.setTag(R.id.mEdit, jsonObject?.get("editable").toString() ?:"" )
        }
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val newObjarray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            val mediaList = JSONObject()
            mediaList.put("mediaList", newObjarray)
            media_recyclerview_list.setTag(R.id.mediaId, mediaList.toString())
        } else {
            media_recyclerview_list.setTag(R.id.mediaId, "{}")
        }
        if (jsonObject?.has("formId") == true) {
            add_btn.setTag(R.id.mediaId, jsonObject.getString("formId"))
            add_btn.setTag(R.id.element, media_recyclerview_list)
            add_signature.setTag(R.id.mediaId, jsonObject.getString("formId"))
            add_signature.setTag(R.id.element, media_recyclerview_list)
            //String formId = jsonObject.getString("formId");
            //  new DynamicResponser(linear_container, formId);
        }
        add_btn.setOnClickListener(listener)
        add_signature.setOnClickListener(listener)
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
        var counter = 0
        val linear_container = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.id = ViewUtil.generateViewId()
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        val main_container =
            linear_container.findViewById<View>(R.id.media_container) as LinearLayout
        val add_btn = linear_container.findViewById<View>(R.id.add_btn) as ImageView
        val add_signature = linear_container.findViewById<View>(R.id.add_signature) as ImageView
        add_btn.setTag(R.id.key, jsonObject?.getString("id"))
        add_btn.setTag(R.id.type, jsonObject?.getString("type"))
        add_signature.setTag(R.id.key, jsonObject?.getString("id"))
        add_signature.setTag(R.id.type, jsonObject?.getString("type"))
        if (jsonObject?.has("mi") == true) {
            val singleMultiple = jsonObject.getString("mi")
            if (!singleMultiple.isEmpty() && "true".equals(singleMultiple, ignoreCase = true)) {
                add_btn.visibility = View.VISIBLE
                add_signature.visibility = View.GONE
            } else {
                add_btn.visibility = View.GONE
                add_signature.visibility = View.VISIBLE
            }
        } else {
            add_btn.visibility = View.GONE
            add_signature.visibility = View.VISIBLE
        }
        if (jsonObject?.has("disabled") == true) {
            add_signature.isEnabled = false
            add_signature.isClickable = false
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
        val media_recyclerview_list: RecyclerView =
            main_container.findViewById<View>(R.id.media_listing) as RecyclerView
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        media_recyclerview_list.setLayoutManager(lm)
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                linear_container.setTag(R.id.v_required, requiredValue)
                linear_container.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }
        counter++
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val newObjarray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            if (newObjarray.length() > 0) {
                val mediaObj = newObjarray.getJSONObject(0)
                if (mediaObj.has("mediaId")) {
                    media_recyclerview_list.setTag(R.id.mediaId, mediaObj.getString("mediaId"))
                }
            }
        }
        //  if (!jsonObject.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
        val id_key = jsonObject?.getString("id") + "_" + counter
        media_recyclerview_list.setTag(R.id.key, id_key)
        if (jsonObject?.has("editable") == true) {
            media_recyclerview_list.setTag(R.id.mEdit, jsonObject?.get("editable").toString() ?:"" )
        }
        if (jsonObject?.has(JsonFormConstants.OPTIONS_FIELD_NAME) == true) {
            val newObjarray = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
            val mediaList = JSONObject()
            mediaList.put("mediaList", newObjarray)
            media_recyclerview_list.setTag(R.id.mediaId, mediaList.toString())
        } else {
            media_recyclerview_list.setTag(R.id.mediaId, "{}")
        }
        if (jsonObject?.has("formId") == true) {
            add_btn.setTag(R.id.mediaId, jsonObject.getString("formId"))
            add_btn.setTag(R.id.element, media_recyclerview_list)
            add_signature.setTag(R.id.mediaId, jsonObject.getString("formId"))
            add_signature.setTag(R.id.element, media_recyclerview_list)
            //String formId = jsonObject.getString("formId");
            //  new DynamicResponser(linear_container, formId);
        }
        add_btn.setOnClickListener(listener)
        add_signature.setOnClickListener(listener)
        return linear_container
    }
}