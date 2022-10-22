package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.FormUtils
import com.demo.dynamicjsonui.dynamicui.utils.ImageUtils
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class ImagePickerFactory : FormWidgetFactory {


    companion object {
        fun validate(imageView: ImageView): ValidationStatus {
            if (imageView.getTag(R.id.v_required) !is String || imageView.getTag(R.id.error) !is String) {
                return ValidationStatus(true, null)
            }
            val isRequired = imageView.getTag(R.id.v_required) as Boolean
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val path = imageView.getTag(R.id.imagePath)
            return if (path is String && !TextUtils.isEmpty(path)) {
                ValidationStatus(true, null)
            } else ValidationStatus(false, imageView.getTag(R.id.error) as String)
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
        val imageView = ImageView(context)
        imageView.setImageDrawable(context?.resources?.getDrawable(R.drawable.menu))
        imageView.setTag(R.id.key, jsonObject?.getString("key"))
        imageView.setTag(R.id.type, jsonObject?.getString("type"))
        val requiredObject = jsonObject?.optJSONObject("v_required")
        if (requiredObject != null) {
            val requiredValue = requiredObject.getString("value")
            if (!TextUtils.isEmpty(requiredValue)) {
                imageView.setTag(R.id.v_required, requiredValue)
                imageView.setTag(R.id.error, requiredObject.optString("err"))
            }
        }
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.layoutParams = context
            ?.resources?.getDimension(R.dimen.default_bottom_margin)?.toInt()?.let {
                FormUtils.getLayoutParams(
                FormUtils.MATCH_PARENT, FormUtils.dpToPixels(context, 200F), 0, 0, 0, it
            )
            }
        val imagePath = jsonObject?.optString("value")
        if (!TextUtils.isEmpty(imagePath)) {
            imageView.setTag(R.id.imagePath, imagePath)
            imageView.setImageBitmap(
                ImageUtils.loadBitmapFromFile(
                    imagePath,
                    ImageUtils.getDeviceWidth(context),
                    FormUtils.dpToPixels(context!!, 200F)
                )
            )
        }
        views.add(imageView)
        val uploadButton = Button(context)
        uploadButton.text = jsonObject?.getString("uploadButtonText")
        uploadButton.layoutParams = context
            ?.resources?.getDimension(R.dimen.default_bottom_margin)?.toInt()?.let {
                FormUtils.getLayoutParams(
                FormUtils.WRAP_CONTENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
            )
            }
        uploadButton.setOnClickListener(listener)
        uploadButton.setTag(R.id.key, jsonObject?.getString("key"))
        uploadButton.setTag(R.id.type, jsonObject?.getString("type"))
        views.add(uploadButton)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {

        val linear_container: LinearLayout = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()

        val imageView = ImageView(context)
        imageView.setImageDrawable(context?.resources?.getDrawable(R.drawable.menu))
        imageView.setTag(R.id.key, jsonObject?.getString("key"))
        imageView.setTag(R.id.type, jsonObject?.getString("type"))
        val requiredObject = jsonObject?.optJSONObject("v_required")
        if (requiredObject != null) {
            val requiredValue = requiredObject.getString("value")
            if (!TextUtils.isEmpty(requiredValue)) {
                imageView.setTag(R.id.v_required, requiredValue)
                imageView.setTag(R.id.error, requiredObject.optString("err"))
            }
        }
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.layoutParams = context
            ?.resources?.getDimension(R.dimen.default_bottom_margin)?.toInt()?.let {
                FormUtils.getLayoutParams(
                    FormUtils.MATCH_PARENT, FormUtils.dpToPixels(context, 200F), 0, 0, 0, it
                )
            }
        val imagePath = jsonObject?.optString("value")
        if (!TextUtils.isEmpty(imagePath)) {
            imageView.setTag(R.id.imagePath, imagePath)
            imageView.setImageBitmap(
                ImageUtils.loadBitmapFromFile(
                    imagePath,
                    ImageUtils.getDeviceWidth(context),
                    FormUtils.dpToPixels(context!!, 200F)
                )
            )
        }
        linear_container.addView(imageView)
        val uploadButton = Button(context)
        uploadButton.text = jsonObject?.getString("uploadButtonText")
        uploadButton.layoutParams = context
            ?.resources?.getDimension(R.dimen.default_bottom_margin)?.toInt()?.let {
                FormUtils.getLayoutParams(
                    FormUtils.WRAP_CONTENT, FormUtils.WRAP_CONTENT, 0, 0, 0, it
                )
            }
        uploadButton.setOnClickListener(listener)
        uploadButton.setTag(R.id.key, jsonObject?.getString("key"))
        uploadButton.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.addView(uploadButton)
        return linear_container
    }
}