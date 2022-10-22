package com.demo.dynamicjsonui.dynamicui.interfaces

import android.content.Context
import android.view.View
import kotlin.Throws
import org.json.JSONObject
import java.lang.Exception

/**
 * Created by Abhishek at 03/10/2022
 */
interface FormWidgetFactory {
    @Throws(Exception::class)
    fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?>?


    @Throws(Exception::class)
    fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View
}