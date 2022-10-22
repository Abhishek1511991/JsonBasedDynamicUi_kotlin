package com.demo.dynamicjsonui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.fragments.JsonFormFragment
import com.demo.dynamicjsonui.dynamicui.interfaces.JsonApi
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), JsonApi {
    private var mJSONObject: JSONObject? = null
    companion object{
        private const val TAG = "HomeActivity_Form"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            try {
                val mjson: String? = loadJSONFromAsset(this, "data.json")
                init(mjson)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            supportFragmentManager.beginTransaction()
                .add(
                    R.id.mainFragmentContainer,
                    JsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME)
                ).commit()
        } else {
            init(savedInstanceState.getString("jsonState"))
        }
    }

    // -------------------start dynamic form handling abhishek ------------------//
    fun loadJSONFromAsset(context: Context, fileName: String?): String? {
        var json: String? = null
        json = try {
            val `is` = context.assets.open(fileName!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            Log.d("TAG", "Exception Occurred : " + ex.message)
            return null
        }
        return json
    }

    fun init(json: String?) {
        try {
            mJSONObject = json?.let { JSONObject(it) }
        } catch (e: JSONException) {
            Log.d(
                Companion.TAG,
                "Initialization error. Json passed is invalid : " + e.message
            )
        }
    }

    override fun getStep(stepName: String?): JSONObject? {
        synchronized(mJSONObject!!) {
            try {
                return mJSONObject!!.getJSONObject(stepName)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun writeValue(stepName: String?, key: String?, value: String?) {
        synchronized(mJSONObject!!) {
            val jsonObject = mJSONObject!!.getJSONObject(stepName)
            val fields = jsonObject.getJSONArray("fields")
            for (i in 0 until fields.length()) {
                val item = fields.getJSONObject(i)
                val keyAtIndex = item.getString("id")
                val keyType = item.getString("type")
                if (keyType.equals("select", ignoreCase = true)) {
                    val optionsArray = item.getJSONArray("options")
                    for (m in 0 until optionsArray.length()) {
                        val moptObj = optionsArray.getJSONObject(m)
                        moptObj.put("id", key + "_" + (m + 1))
                    }
                }
                if (key == keyAtIndex) {
                    item.put("default", value)
                    return
                }
            }
        }
    }

    override fun writeValue(
        stepName: String?,
        parentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        synchronized(mJSONObject!!) {
            val jsonObject = mJSONObject!!.getJSONObject(stepName)
            val fields = jsonObject.getJSONArray("fields")
            for (i in 0 until fields.length()) {
                val item = fields.getJSONObject(i)
                val keyAtIndex = item.getString("id")
                val keyType = item.getString("type")
                if (parentKey == keyAtIndex) {
                    val jsonArray = item.getJSONArray(childObjectKey)
                    if (keyType.equals("select", ignoreCase = true)) {
                        for (m in 0 until jsonArray.length()) {
                            val moptObj = jsonArray.getJSONObject(m)
                            moptObj.put("id", parentKey + "_" + (m + 1))
                        }
                    }
                    for (j in 0 until jsonArray.length()) {
                        val innerItem = jsonArray.getJSONObject(j)
                        val amnotherKeyAtIndex = innerItem.getString("id")
                        if (keyType.equals("select", ignoreCase = true)) {
                            if (childKey == amnotherKeyAtIndex) {
                                innerItem.put("default", "true")
                                //  return;
                            } else {
                                innerItem.put("default", "false")
                            }
                        } else {
                            if (childKey == amnotherKeyAtIndex) {
                                innerItem.put("default", value)
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    override fun writeValueMulti(
        stepName: String?,
        parentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        synchronized(mJSONObject!!) {
            val jsonObject = mJSONObject!!.getJSONObject(stepName)
            val fields = jsonObject.getJSONArray("fields")
            for (i in 0 until fields.length()) {
                val item = fields.getJSONObject(i)
                val keyAtIndex = item.getString("id")
                if (parentKey == keyAtIndex) {
                    Log.d("abc", "df")
                    if (item.has(childObjectKey)) {
                        val jsonArray = item.getJSONArray(childObjectKey)
                        val newjsonobj = JSONObject()
                        newjsonobj.put("id", childKey)
                        newjsonobj.put("default", value)
                        jsonArray.put(newjsonobj)
                    } else {
                        val newjsonarray = JSONArray()
                        val newjsonobj = JSONObject()
                        newjsonobj.put("id", childKey)
                        newjsonobj.put("default", value)
                        newjsonarray.put(newjsonobj)
                        item.put(childObjectKey, newjsonarray)
                    }
                }
            }
        }
    }

    override fun writeValueMultiChange(
        stepName: String?,
        parentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        synchronized(mJSONObject!!) {
            val jsonObject = mJSONObject!!.getJSONObject(stepName)
            val fields = jsonObject.getJSONArray("fields")
            for (i in 0 until fields.length()) {
                val item = fields.getJSONObject(i)
                val keyAtIndex = item.getString("id")
                if (parentKey == keyAtIndex) {
                    Log.d("abc", "df")
                    if (item.has(childObjectKey)) {
                        val jsonArray = item.getJSONArray(childObjectKey)
                        val newjsonobj = JSONObject()
                        newjsonobj.put("id", childKey)
                        newjsonobj.put("default", value)
                        jsonArray.put(newjsonobj)
                    } else {
                        val newjsonarray = JSONArray()
                        val newjsonobj = JSONObject()
                        newjsonobj.put("id", childKey)
                        //  newjsonobj.put("fieldContent", childKey);
                        newjsonobj.put("default", value)
                        newjsonarray.put(newjsonobj)
                        item.put(childObjectKey, newjsonarray)
                    }
                }
            }
        }
    }

    override fun writeValueMultiCheck(
        stepName: String?,
        parentKey: String?,
        mparentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        synchronized(mJSONObject!!) {
            val jsonObject = mJSONObject!!.getJSONObject(stepName)
            val fields = jsonObject.getJSONArray("fields")
            for (i in 0 until fields.length()) {
                val item = fields.getJSONObject(i)
                val keyAtIndex = item.getString("id")
                val keyType = item.getString("type")
                if (keyType.equals(JsonFormConstants.MULTI_CHECKBOX, ignoreCase = true)) {
                    if (parentKey == keyAtIndex) {
                        val jsonArray = item.getJSONArray(childObjectKey)
                        for (j in 0 until jsonArray.length()) {
                            val innerItem = jsonArray.getJSONObject(j)
                            val amnotherKeyAtIndex = innerItem.getString("id")
                            if (mparentKey == amnotherKeyAtIndex) {
                                if (innerItem.has("default") && !innerItem.getString("default")
                                        .isEmpty() && !innerItem.getString("default")
                                        .equals("", ignoreCase = true)
                                ) {
                                    val abca =
                                        innerItem.getJSONArray("default")
                                    if (abca != null) {
                                        var ib = false
                                        var isin: Int? = null
                                        for (p in 0 until abca.length()) {
                                            val mstr = abca.getString(p)
                                            if (value.equals(mstr, ignoreCase = true)) {
                                                ib = true
                                                isin = p
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    val abca = JSONArray()
                                    abca.put(value)
                                    innerItem.put("default", abca)
                                }
                            }
                        }
                    }
                }
                else {

                }
            }
        }
    }

    override fun dynamicResponser(formId: String?) {
        TODO("Not yet implemented")
    }

    override fun currentJsonState(): String? {
        synchronized(mJSONObject!!) { return mJSONObject.toString() }
    }

    override val count: String?
        get() = synchronized(mJSONObject!!) { return mJSONObject!!.optString("count") }

    fun hideKeyboard(){

    }

}

