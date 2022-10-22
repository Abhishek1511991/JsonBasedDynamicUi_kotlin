package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.custom_views.RadioButton
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.FormUtils

import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class RadioButtonFactory : FormWidgetFactory {
    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        val views: MutableList<View> = ArrayList(1)
        val linear_container =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.addView(FormUtils.getTextViewWith(
            context,
            16,
            jsonObject?.getString("title"),
            jsonObject?.getString("id"),
            jsonObject?.getString("type"),
            FormUtils.getLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                FormUtils.WRAP_CONTENT,
                0,
                0,
                0,
                0
            ),
            FormUtils.FONT_BOLD_PATH
        ))
        val options = jsonObject?.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
        for (i in 0 until options?.length()!!) {
            val item = options.getJSONObject(i)
            val radioButton: RadioButton = LayoutInflater.from(context).inflate(
                R.layout.item_radiobutton,
                null
            ) as RadioButton
            radioButton.setText(item.getString("displayText"))
            radioButton.setTag(R.id.key, jsonObject.getString("id"))
            radioButton.setTag(R.id.type, jsonObject.getString("type"))
            radioButton.setTag(R.id.childKey, item.getString("fieldContent"))
            radioButton.setGravity(Gravity.CENTER_VERTICAL)
            radioButton.setTextSize(16F)
            radioButton.setTextColor(Color.BLACK)
            radioButton.setTypeface(
                Typeface.createFromAsset(
                    context?.assets,
                    FormUtils.FONT_REGULAR_PATH
                )
            )
            radioButton.setOnCheckedChangeListener(listener)
            if (!TextUtils.isEmpty(jsonObject.optString("value"))
                && jsonObject.optString("value") == item.getString("id")
            ) {
                radioButton.setChecked(true)
            }
            if (i == options.length() - 1) {
                radioButton.setLayoutParams(
                    context
                        ?.resources?.getDimension(R.dimen.extra_bottom_margin)?.toInt()?.let {
                            FormUtils.getLayoutParams(
                                FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
                            )
                        }
                )
            }
            linear_container.addView(radioButton)
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

        val linear_container =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout

        linear_container.addView(FormUtils.getTextViewWith(
            context,
            16,
            jsonObject?.getString("title"),
            jsonObject?.getString("id"),
            jsonObject?.getString("type"),
            FormUtils.getLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                FormUtils.WRAP_CONTENT,
                0,
                0,
                0,
                0
            ),
            FormUtils.FONT_BOLD_PATH
        ))


        val options = jsonObject?.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
        for (i in 0 until options?.length()!!) {
            val item = options.getJSONObject(i)
            val radioButton: RadioButton = LayoutInflater.from(context).inflate(
                R.layout.item_radiobutton,
                null
            ) as RadioButton
            radioButton.setText(item.getString("displayText"))
            radioButton.setTag(R.id.key, jsonObject.getString("id"))
            radioButton.setTag(R.id.type, jsonObject.getString("type"))
            radioButton.setTag(R.id.childKey, item.getString("fieldContent"))
            radioButton.setGravity(Gravity.CENTER_VERTICAL)
            radioButton.setTextSize(16F)
            radioButton.setTextColor(Color.BLACK)
            radioButton.setTypeface(
                Typeface.createFromAsset(
                    context?.assets,
                    FormUtils.FONT_REGULAR_PATH
                )
            )
            radioButton.setOnCheckedChangeListener(listener)
            if (!TextUtils.isEmpty(jsonObject.optString("value"))
                && jsonObject.optString("value") == item.getString("id")
            ) {
                radioButton.setChecked(true)
            }
            if (i == options.length() - 1) {
                radioButton.setLayoutParams(
                    context
                        ?.resources?.getDimension(R.dimen.extra_bottom_margin)?.toInt()?.let {
                            FormUtils.getLayoutParams(
                                FormUtils.MATCH_PARENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
                            )
                        }
                )
            }
            linear_container.addView(radioButton)
        }
        return linear_container
    }

}