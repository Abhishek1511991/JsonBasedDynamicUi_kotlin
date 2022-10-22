package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class ImageFactory : FormWidgetFactory {

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        var counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container: LinearLayout = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()

        if (jsonObject?.has("url") == true) {
            val imageView = ImageView(context)
            imageView.scaleType=ImageView.ScaleType.FIT_XY
            imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) // value is in pixels
            Glide.with(imageView.context).load(Uri.parse(jsonObject.getString("url"))).placeholder(R.drawable.placeholder).into(imageView)
            linear_container.addView(imageView)
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
        val linear_container: LinearLayout = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()
        if (jsonObject?.has("url") == true) {
            val imageView = ImageView(context)
            imageView.scaleType=ImageView.ScaleType.FIT_XY
            imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) // value is in pixels
            Glide.with(imageView.context).load(Uri.parse(jsonObject.getString("url"))).placeholder(R.drawable.placeholder).into(imageView)
            linear_container.addView(imageView)
        }
        return linear_container
    }
}