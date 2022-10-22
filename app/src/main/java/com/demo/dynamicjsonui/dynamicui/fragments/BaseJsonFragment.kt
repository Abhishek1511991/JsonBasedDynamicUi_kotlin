package com.demo.dynamicjsonui.dynamicui.fragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.demo.dynamicjsonui.MainActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

open class BaseJsonFragment:Fragment() {

    @Throws(JSONException::class)
    fun getCovertedData(data: String?, activity: MainActivity, context: Context): String {
        var newjo: JSONObject? = null
        val mainjsonObj = JSONObject(data)
        val abcarray = mainjsonObj.getJSONArray("data")
        val zxObj = abcarray.getJSONObject(0)
        val jsonArray = zxObj.getJSONArray("field")
        val stringsObj = zxObj.getJSONObject("strings")
        val lngObj = stringsObj.getJSONObject("en")
        for (x in 0 until jsonArray.length()) {
            val mobj = jsonArray.getJSONObject(x)
            if (mobj.has("title")) {
                val mVal = mobj.getString("title") as String
                if (!mVal.isEmpty() && !mVal.equals("", ignoreCase = true)) {
                    if (lngObj.has(mVal)) {
                        mobj.put("title", lngObj.getString(mVal))
                    }
                }
            }
            if (mobj.has("placeholder")) {
                val mVal = mobj.getString("placeholder") as String
                if (!mVal.isEmpty() && !mVal.equals("", ignoreCase = true)) {
                    if (lngObj.has(mVal)) {
                        mobj.put("placeholder", lngObj.getString(mVal))
                    }
                }
            }
            if (mobj.has("description")) {
                val mVal = mobj.getString("description") as String
                if (!mVal.isEmpty() && !mVal.equals("", ignoreCase = true)) {
                    if (lngObj.has(mVal)) {
                        mobj.put("description", lngObj.getString(mVal))
                    }
                }
            }
            if (mobj.has("defaultValue")) {
                val mVal = mobj.getString("defaultValue") as String
                if (!mVal.isEmpty() && !mVal.equals("", ignoreCase = true)) {
                    if (lngObj.has(mVal)) {
                        mobj.put("defaultValue", lngObj.getString(mVal))
                    }
                }
            }
            if (mobj.has("validation")) {
                val valdObj = mobj.getJSONObject("validation")
                if (valdObj.has("errorMessage")) {
                    val mVal = valdObj.getString("errorMessage") as String
                    if (!mVal.isEmpty() && !mVal.equals("", ignoreCase = true)) {
                        if (lngObj.has(mVal)) {
                            valdObj.put("errorMessage", lngObj.getString(mVal))
                        }
                    }
                }
            }
            if (mobj.has("options")) {
                val optionArray = mobj.getJSONArray("options")
                for (n in 0 until optionArray.length()) {
                    val optionObj = optionArray.getJSONObject(n)
                    if (optionObj.has("displayText")) {
                        val mVal = optionObj.getString("displayText") as String
                        if (!mVal.isEmpty() && !mVal.equals("", ignoreCase = true)) {
                            if (lngObj.has(mVal)) {
                                optionObj.put("displayText", lngObj.getString(mVal))
                            }
                        }
                    }
                }
            }
        }
        if (jsonArray != null && jsonArray.length() > 0) {
            val json: String? = activity.loadJSONFromAsset(context, "data.json")
            newjo = json?.let { JSONObject(it) }
            val fieldArray2 = newjo!!["step1"] as JSONObject
            fieldArray2.put("fields", jsonArray)
        }
        return newjo.toString()
    }

    fun getCombineData(json: String?,mActivity: MainActivity,context: Context): String? {
        var mobj: JSONObject? = null
        val datajson: String? = mActivity.loadJSONFromAsset(context, "data.json")
        try {
            val jsonObjectarray = JSONArray()
            mobj = json?.let { JSONObject(it) }
            val amobj = mobj?.getJSONObject("step1")
//            val majarray = amobj?.getJSONArray("fields")
//            for (i in 0 until majarray?.length()!!) {
//                val abc = majarray.getJSONObject(i)
//                val id = abc?.getString("id")
//                val type = abc?.getString("type")
//                if (type.equals(JsonFormConstants.MULTI_CHECKBOX, ignoreCase = true) || type.equals(
//                        JsonFormConstants.MULTI_RADIO,
//                        ignoreCase = true
//                    ) || type.equals(JsonFormConstants.SPINNER, ignoreCase = true)
//                ) {
//                    if (abc.has(JsonFormConstants.OPTIONS_FIELD_NAME)) {
//                        val optionArray = abc.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME)
//                        for (m in 0 until optionArray.length()) {
//                            val optionObj = optionArray.getJSONObject(m)
//                            val fieldContent = optionObj.getString("fieldContent")
//                            // String mdefault = optionObj.getString("default");
//                            for (x in 0 until jsonObjectarray.length()) {
//                                val mob = jsonObjectarray.getJSONObject(x)
//                                val mid = mob.getString("id")
//                                if (mid.equals(id, ignoreCase = true)) {
//                                    if (mob.has("fields")) {
//                                        val fieldArray = mob.getJSONArray("fields")
//                                        for (o in 0 until fieldArray.length()) {
//                                            val fieldOob = fieldArray.getJSONObject(o)
//                                            val mVal = fieldOob.getString("val")
//                                            if (fieldContent.equals(mVal, ignoreCase = true)) {
//                                                optionObj.put("default", "true")
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    for (x in 0 until jsonObjectarray.length()) {
//                        val mob = jsonObjectarray.getJSONObject(x)
//                        val mid = mob.getString("id")
//                        if (mid.equals(id, ignoreCase = true)) {
//                            val marray = mob.getJSONArray("fields")
//                            abc.put("options", marray)
//                            break
//                        }
//                    }
//                }
//            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Log.d("testing", "dfdf")
        return mobj.toString()
    }
}