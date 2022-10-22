package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class LinearFactory: FormWidgetFactory {
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
        linear_container.setBackgroundResource(R.drawable.border)

        val linearLayout = context?.let { LinearLayoutCompat(it) }?.apply {
            layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT) // value is in pixels
        }

        if(jsonObject?.has("weight_sum") == true){
            linearLayout?.weightSum = jsonObject.getString("weight_sum").toFloat()
        }

        counter++
        linearLayout?.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)

        if(jsonObject?.has("orientation") == true){
             when(jsonObject.getString("orientation") ){

                 "linear"-> linearLayout?.orientation= LinearLayoutCompat.VERTICAL
                 "horizontal"-> linearLayout?.orientation= LinearLayoutCompat.HORIZONTAL

             }
        }

        if (jsonObject?.has("data") == true) {
            val jsonArray = jsonObject.getJSONArray("data")
            for (i in 0..jsonArray.length()-1) {
                val childJson =jsonArray.getJSONObject(i)
                val childVies: View? = map[childJson.getString("type")]?.getViewFromJson(
                    map,
                    stepName,
                    context,
                    childJson,
                    listener
                )
                linearLayout?.addView(childVies)
            }
        }

        linear_container.addView(linearLayout)
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
        val views: MutableList<View> = ArrayList(1)
        val linear_container: LinearLayout = LayoutInflater.from(context)
            .inflate(R.layout.signature_layout_factory, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()
        linear_container.setBackgroundResource(R.drawable.border)

        val linearLayout = context?.let { LinearLayoutCompat(it) }?.apply {
            layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT) // value is in pixels
        }

        if(jsonObject?.has("weight_sum") == true){
            linearLayout?.weightSum = jsonObject.getString("weight_sum").toFloat()
        }

        counter++
        linearLayout?.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)

        if(jsonObject?.has("orientation") == true){
            when(jsonObject.getString("orientation") ){

                "linear"-> linearLayout?.orientation= LinearLayoutCompat.VERTICAL
                "horizontal"-> linearLayout?.orientation= LinearLayoutCompat.HORIZONTAL

            }
        }

        if (jsonObject?.has("data") == true) {
            val jsonArray = jsonObject.getJSONArray("data")
            for (i in 0..jsonArray.length()-1) {
                val childJson =jsonArray.getJSONObject(i)
                val childVies: View? = map[childJson.getString("type")]?.getViewFromJson(
                    map,
                    stepName,
                    context,
                    childJson,
                    listener
                )
                childVies?.setPadding(10)
                linearLayout?.addView(childVies)
            }
        }

        linear_container.addView(linearLayout)
        return linear_container
    }
}