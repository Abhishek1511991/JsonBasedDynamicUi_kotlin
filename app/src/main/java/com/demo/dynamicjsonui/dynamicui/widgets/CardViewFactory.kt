package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject

class CardViewFactory: FormWidgetFactory {

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {

        val views: MutableList<View> = ArrayList(1)
        val card_container: MaterialCardView =
            LayoutInflater.from(context).inflate(R.layout.card_layout_edit, null) as MaterialCardView

        card_container.id= ViewUtil.generateViewId()
        val linearLayout= context?.let { LinearLayoutCompat(it) }
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 10, 10, 10)
        linearLayout?.layoutParams= layoutParams
        linearLayout?.setPadding(10)

        if (jsonObject?.has("view_type") == true) {
            when(jsonObject.getString("view_type")){
                "linear" ->  linearLayout?.orientation= LinearLayoutCompat.VERTICAL
                "horizontal"->  linearLayout?.orientation=LinearLayoutCompat.HORIZONTAL
            }

        }

        if (jsonObject?.has("data") == true) {
            val jsonArray = jsonObject.getJSONArray("data")
            Log.e("size","--->"+jsonArray.length())
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
        card_container.addView(linearLayout)
        views.add(card_container)
        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        val views: MutableList<View> = ArrayList(1)
        val card_container: MaterialCardView =
            LayoutInflater.from(context).inflate(R.layout.card_layout_edit, null) as MaterialCardView

        card_container.id= ViewUtil.generateViewId()
        val linearLayout= context?.let { LinearLayoutCompat(it) }
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 10, 10, 10)
        linearLayout?.layoutParams= layoutParams
        linearLayout?.setPadding(10)

        if (jsonObject?.has("view_type") == true) {
            when(jsonObject.getString("view_type")){
                "linear" ->  linearLayout?.orientation= LinearLayoutCompat.VERTICAL
                "horizontal"->  linearLayout?.orientation=LinearLayoutCompat.HORIZONTAL
            }

        }

        if (jsonObject?.has("data") == true) {
            val jsonArray = jsonObject.getJSONArray("data")
            Log.e("size","--->"+jsonArray.length())
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
        card_container.addView(linearLayout)
        return card_container
    }
}