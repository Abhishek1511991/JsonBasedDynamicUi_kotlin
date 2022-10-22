package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class ButtonFactory: FormWidgetFactory {
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

        val button = Button(context)

        val layoutParams= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) // value is in pixels

        if(jsonObject?.has("weight") == true){
            layoutParams.weight = jsonObject.getString("weight").toFloat()
            linear_container.weightSum= jsonObject.getString("weight").toFloat()
        }
        button.layoutParams = layoutParams
        counter++
        button.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
        if(jsonObject?.has("text_size") == true){
            button.textSize= jsonObject.getString("text_size").toFloat()
        }
        if (jsonObject?.has("title") == true) {
            button.setText(jsonObject.getString("title"))
        }
        if(jsonObject?.has("text_color") == true){
            button.setTextColor(Color.parseColor(jsonObject.getString("text_color")))
        }
        else
            button.setTextColor(Color.parseColor("#3498DB"))

        if(jsonObject?.has("url") == true){
            if (context != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(jsonObject.getString("url"))
                    .into(object : CustomTarget<Bitmap?>() {


                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                        ) {
                            val d: Drawable = BitmapDrawable(context.getResources(), resource)
                            if(jsonObject.has("icon_position") == true){
                                when(jsonObject.getString("icon_position")){

                                    "left"-> { button.setCompoundDrawables(d, null, null, null)}
                                    "right"-> { button.setCompoundDrawables(null, null, d, null)}
                                    "top"-> { button.setCompoundDrawables(null, d, null, null)}
                                    "bottom"-> { button.setCompoundDrawables(null, null, null, d)}
                                    "center"-> {}
                                }
                            }
                        }
                    })
            }
        }



        linear_container.addView(button)
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
        val linear_container: LinearLayout = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()

        val button = Button(context)

        val layoutParams= LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) // value is in pixels

        if(jsonObject?.has("weight") == true){
            layoutParams.weight = jsonObject.getString("weight").toFloat()
        }
        button.layoutParams = layoutParams
        counter++
        button.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
        if(jsonObject?.has("text_size") == true){
            button.textSize= jsonObject.getString("text_size").toFloat()
        }
        if (jsonObject?.has("title") == true) {
            button.setText(jsonObject.getString("title"))
        }
        if(jsonObject?.has("text_color") == true){
            button.setTextColor(Color.parseColor(jsonObject.getString("text_color")))
        }
        else
            button.setTextColor(Color.parseColor("#3498DB"))
        if(jsonObject?.has("url") == true){
            if (context != null) {
                Glide.with(context)
                    .asBitmap()
                    .load(jsonObject.getString("url"))
                    .into(object : CustomTarget<Bitmap?>() {


                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                        ) {
                            val d: Drawable = BitmapDrawable(context.getResources(), resource)
                            if(jsonObject.has("icon_position") == true){
                                when(jsonObject.getString("icon_position")){

                                    "left"-> { button.setCompoundDrawables(d, null, null, null)}
                                    "right"-> { button.setCompoundDrawables(null, null, d, null)}
                                    "top"-> { button.setCompoundDrawables(null, d, null, null)}
                                    "bottom"-> { button.setCompoundDrawables(null, null, null, d)}
                                    "center"-> {}
                                }
                            }
                        }
                    })
            }
        }
        linear_container.addView(button)
        return linear_container
    }
}