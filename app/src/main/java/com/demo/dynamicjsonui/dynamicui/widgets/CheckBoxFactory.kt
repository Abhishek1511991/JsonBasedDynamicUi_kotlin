package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.FormUtils
import org.json.JSONObject

/**
 * Created by Abhishek at 18/10/2022
 */
class CheckBoxFactory : FormWidgetFactory {

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        val views: MutableList<View> = ArrayList(1)
        views.add(
            FormUtils.getTextViewWith(
                context,
                16,
                jsonObject?.getString("title"),
                jsonObject?.getString("id"),
                jsonObject?.getString("type"),
                FormUtils.getLayoutParams(
                    FormUtils.MATCH_PARENT,
                    FormUtils.WRAP_CONTENT,
                    0,
                    0,
                    0,
                    0
                ),
                FormUtils.FONT_BOLD_PATH
            )
        )
        val options = jsonObject?.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
        if(options?.length()!! >0) {
            for (i in 0 until options?.length()!!) {
                val item = options.getJSONObject(i)
                val checkBox: CheckBox =
                    LayoutInflater.from(context).inflate(R.layout.item_checkbox, null) as CheckBox
                checkBox.setText(item.getString("displayText"))
                checkBox.setTag(R.id.key, jsonObject.getString("id"))
                checkBox.setTag(R.id.type, jsonObject.getString("type"))
                checkBox.setTag(R.id.childKey, item.getString("fieldContent"))
                checkBox.setGravity(Gravity.CENTER_VERTICAL)
                checkBox.setTextSize(16F)
                checkBox.setTextColor(Color.BLACK)
                checkBox.setTypeface(
                    Typeface.createFromAsset(
                        context?.assets,
                        FormUtils.FONT_REGULAR_PATH
                    )
                )
                checkBox.setOnCheckedChangeListener(listener)
                if (!TextUtils.isEmpty(item.optString("default"))) {
                    checkBox.setChecked(java.lang.Boolean.valueOf(item.optString("default")))
                }
                if (i == options.length() - 1) {
                    checkBox.setLayoutParams(
                        context
                            ?.resources?.getDimension(R.dimen.extra_bottom_margin)?.toInt()?.let {
                                FormUtils.getLayoutParams(
                                    FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
                                )
                            }
                    )
                }
                val mrequiredObject = jsonObject.optJSONObject("validation")
                if (mrequiredObject != null) {
                    val requiredValue = mrequiredObject.getString("required")
                    if (!TextUtils.isEmpty(requiredValue)) {
                        checkBox.setTag(R.id.v_required, requiredValue)
                        checkBox.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
                    }
                }
                views.add(checkBox)
            }
        }
        else{
            val checkBox: CheckBox =
                LayoutInflater.from(context).inflate(R.layout.item_checkbox, null) as CheckBox
            checkBox.setText(jsonObject.getString("displayText"))
            checkBox.setTag(R.id.key, jsonObject.getString("id"))
            checkBox.setTag(R.id.type, jsonObject.getString("type"))
            checkBox.setTag(R.id.childKey, jsonObject.getString("fieldContent"))
            checkBox.setGravity(Gravity.CENTER_VERTICAL)
            checkBox.setTextSize(16F)
            checkBox.setTextColor(Color.BLACK)
            checkBox.setTypeface(
                Typeface.createFromAsset(
                    context?.assets,
                    FormUtils.FONT_REGULAR_PATH
                )
            )
            checkBox.setOnCheckedChangeListener(listener)
            if (!TextUtils.isEmpty(jsonObject.optString("default"))) {
                checkBox.setChecked(java.lang.Boolean.valueOf(jsonObject.optString("default")))
            }
            checkBox.setLayoutParams(
                context
                    ?.resources?.getDimension(R.dimen.extra_bottom_margin)?.toInt()?.let {
                        FormUtils.getLayoutParams(
                            FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
                        )
                    }
            )
            val mrequiredObject = jsonObject.optJSONObject("validation")
            if (mrequiredObject != null) {
                val requiredValue = mrequiredObject.getString("required")
                if (!TextUtils.isEmpty(requiredValue)) {
                    checkBox.setTag(R.id.v_required, requiredValue)
                    checkBox.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
                }
            }
            views.add(checkBox)
        }
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        val checkBox: CheckBox =
            LayoutInflater.from(context).inflate(R.layout.item_checkbox, null) as CheckBox
        checkBox.setText(jsonObject?.getString("displayText"))
        checkBox.setTag(R.id.key, jsonObject?.getString("id"))
        checkBox.setTag(R.id.type, jsonObject?.getString("type"))
        checkBox.setTag(R.id.childKey, jsonObject?.getString("fieldContent"))
        checkBox.setGravity(Gravity.CENTER_VERTICAL)
        checkBox.setTextSize(16F)
        checkBox.setTextColor(Color.BLACK)
        checkBox.setTypeface(
            Typeface.createFromAsset(
                context?.assets,
                FormUtils.FONT_REGULAR_PATH
            )
        )
        checkBox.setOnCheckedChangeListener(listener)
        if (!TextUtils.isEmpty(jsonObject?.optString("default"))) {
            checkBox.setChecked(java.lang.Boolean.valueOf(jsonObject?.optString("default")))
        }
        checkBox.setLayoutParams(
            context
                ?.resources?.getDimension(R.dimen.extra_bottom_margin)?.toInt()?.let {
                    FormUtils.getLayoutParams(
                        FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
                    )
                }
        )
        val mrequiredObject = jsonObject?.optJSONObject("validation")
        if (mrequiredObject != null) {
            val requiredValue = mrequiredObject.getString("required")
            if (!TextUtils.isEmpty(requiredValue)) {
                checkBox.setTag(R.id.v_required, requiredValue)
                checkBox.setTag(R.id.error, mrequiredObject.optString("errorMessage"))
            }
        }

        return checkBox
    }
}