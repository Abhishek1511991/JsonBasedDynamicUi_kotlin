package com.demo.dynamicjsonui.dynamicui.widgets

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.demo.dynamicjsonui.dynamicui.utils.ViewUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by Abhishek at 03/10/2022
 */
class LabelFactory : FormWidgetFactory {
    var counter = 0


    companion object {
        const val MIN_LENGTH = 0
        const val MAX_LENGTH = 100
        var validator = ArrayList<JSONObject>()
        fun validate(childAt: LinearLayout): ValidationStatus {
            val isRequired = java.lang.Boolean.valueOf(childAt.getTag(R.id.v_required) as String)
            return if (!isRequired) {
                ValidationStatus(true, null)
            } else ValidationStatus(true, null)
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
        val textView = TextView(context)
        if(jsonObject?.has("text_color") == true){
            textView.setTextColor(Color.parseColor(jsonObject.getString("text_color")))
        }
        else
            textView.setTextColor(Color.parseColor("#3498DB"))

        if(jsonObject?.has("text_size") == true){
           textView.textSize= jsonObject.getString("text_size").toFloat()
        }
        if(jsonObject?.has("bold") == true){
           if(jsonObject.getString("bold").equals("true")){
               textView.setTypeface(textView.getTypeface(), Typeface.BOLD)
           }
        }

        if (jsonObject?.has("title") == true) {
            textView.setText(jsonObject.getString("title"))
            textView.setTextSize(20f)

            if(jsonObject.has("text_color") == true){
                textView.setTextColor(Color.parseColor(jsonObject.getString("text_color")))
            }
            else
                textView.setTextColor(Color.parseColor("#3498DB"))
        }

        textView.setLayoutParams(params)
        try {
            counter++
            textView.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        linear_container.addView(textView)
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
        val linear_container: LinearLayout =
            LayoutInflater.from(context).inflate(R.layout.multi_layout_edit, null) as LinearLayout
        linear_container.setTag(R.id.key, jsonObject?.getString("id"))
        linear_container.setTag(R.id.type, jsonObject?.getString("type"))
        linear_container.id= ViewUtil.generateViewId()

        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        counter++
        val textView = TextView(context)
        if(jsonObject?.has("text_color") == true){
            textView.setTextColor(Color.parseColor(jsonObject.getString("text_color")))
        }
        else
            textView.setTextColor(Color.parseColor("#3498DB"))

        if(jsonObject?.has("text_size") == true){
            textView.textSize= jsonObject.getString("text_size").toFloat()
        }
        if(jsonObject?.has("bold") == true){
            if(jsonObject.getString("bold").equals("true")){
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD)
            }
        }

        if (jsonObject?.has("title") == true) {
            textView.setText(jsonObject.getString("title"))
            textView.setTextSize(20f)

            if(jsonObject.has("text_color") == true){
                textView.setTextColor(Color.parseColor(jsonObject.getString("text_color")))
            }
            else
                textView.setTextColor(Color.parseColor("#3498DB"))
        }

        textView.setLayoutParams(params)
        try {
            textView.setTag(R.id.key, jsonObject?.getString("id") + "_" + counter)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        linear_container.addView(textView)
        return linear_container
    }


    fun getDrwableFromUrl(context:Context,url:String):Drawable{
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        val inputStream: InputStream = connection.getInputStream()
        val tempBitmap = BitmapFactory.decodeStream(inputStream)
        return BitmapDrawable(context.getResources(), tempBitmap)
    }



}