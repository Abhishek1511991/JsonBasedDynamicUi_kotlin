package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.rey.material.util.ViewUtil
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class RecylerViewFactory : FormWidgetFactory {
    var counter = 0

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?> {
        counter = 0
        val views: MutableList<View> = ArrayList(1)
        val linear_container: LinearLayout =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val recyclerView = RecyclerView(context!!)
        recyclerView.setFocusableInTouchMode(true)
        recyclerView.setLayoutParams(params)
        if (jsonObject?.has("disabled") == true) {
            recyclerView.setEnabled(false)
        }
        recyclerView.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
        if(jsonObject?.has("orientation") == true){
            when(jsonObject.getString("orientation") ){

                "linear"-> {
                    recyclerView.layoutManager= LinearLayoutManager(context,RecyclerView.VERTICAL,false)
                }
                "horizontal"-> {
                    recyclerView.layoutManager= LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                }
                "grid"-> {
                    if(jsonObject.has("grid_span_count") ==true)
                    {
                        val gridCount=jsonObject.getInt("grid_span_count")
                        recyclerView.layoutManager= GridLayoutManager(context, gridCount)
                    }
                    else{
                        recyclerView.layoutManager= GridLayoutManager(context, 1)
                    }
                }

            }
        }

        if(jsonObject?.has("data_array") == true){

            val array= mutableListOf<String>()
            val jsonArray=jsonObject.getJSONArray("data_array")

            for(i in 0..jsonArray.length()){
                array.add(jsonArray.get(i).toString())
            }
            recyclerView.adapter=CommonAdapter(context =context, items = array.toTypedArray(), listener = listener, jsonObject = jsonObject)

        }
        else{
            recyclerView.adapter=CommonAdapter(context =context, items = emptyArray(), listener = listener, jsonObject = jsonObject)
        }

        linear_container.addView(recyclerView)
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
        val linear_container: LinearLayout =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.setId(ViewUtil.generateViewId())
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val recyclerView = RecyclerView(context!!)
        recyclerView.setFocusableInTouchMode(true)
        recyclerView.setLayoutParams(params)
        if (jsonObject?.has("disabled") == true) {
            recyclerView.setEnabled(false)
        }
        recyclerView.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
        if(jsonObject?.has("orientation") == true){
            when(jsonObject.getString("orientation") ){

                "linear"-> {
                    recyclerView.layoutManager= LinearLayoutManager(context,RecyclerView.VERTICAL,false)
                }
                "horizontal"-> {
                    recyclerView.layoutManager= LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                }
                "grid"-> {
                    if(jsonObject.has("grid_span_count") ==true)
                    {
                        val gridCount=jsonObject.getInt("grid_span_count")
                        recyclerView.layoutManager= GridLayoutManager(context, gridCount)
                    }
                    else{
                        recyclerView.layoutManager= GridLayoutManager(context, 1)
                    }
                }

            }
        }

        if(jsonObject?.has("data_array") == true){

            val array= mutableListOf<String>()
            val jsonArray=jsonObject.getJSONArray("data_array")

            for(i in 0..jsonArray.length()){
                array.add(jsonArray.get(i).toString())
            }
            recyclerView.adapter=CommonAdapter(context =context, items = array.toTypedArray(), listener = listener, jsonObject = jsonObject)

        }
        else{
            recyclerView.adapter=CommonAdapter(context =context, items = emptyArray(), listener = listener, jsonObject = jsonObject)
        }

        linear_container.addView(recyclerView)
        return linear_container
    }

}

class CommonAdapter(val items: Array<String>, val context: Context,
                    val listener: CommonListener?,val jsonObject: JSONObject?) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAnimalType.text = items.get(position)
        holder.tvAnimalType.setTag(R.id.key, jsonObject?.getString("id"))
        holder.tvAnimalType.setTag(R.id.type, jsonObject?.getString("type"))
        holder.tvAnimalType.setOnClickListener {
            listener?.onClick(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_adapter, parent, false))
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvAnimalType = view.findViewById<TextView>(R.id.tv_text)
}