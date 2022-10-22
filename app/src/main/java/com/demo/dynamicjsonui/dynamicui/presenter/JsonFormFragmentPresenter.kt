package com.demo.dynamicjsonui.dynamicui.presenter


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.demo.dynamicjsonui.dynamicui.widgets.EditTextFactory
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.fragments.JsonFormFragment
import com.demo.dynamicjsonui.dynamicui.interactors.JsonFormInteractor
import com.demo.dynamicjsonui.dynamicui.interfaces.MvpBasePresenter
import com.demo.dynamicjsonui.dynamicui.models.ClearFocus
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import com.demo.dynamicjsonui.dynamicui.viewState.JsonFormFragmentViewState
import com.demo.dynamicjsonui.dynamicui.views.JsonFormFragmentView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Abhishek at 03/10/2022
 */
class JsonFormFragmentPresenter :
    MvpBasePresenter<JsonFormFragmentView<JsonFormFragmentViewState?>?>() {
    private var mStepName: String? = null
    private var mStepDetails: JSONObject? = null
    private var mCurrentKey: String? = null
    private val mJsonFormInteractor: JsonFormInteractor = JsonFormInteractor.instance
    var currentTimesapn = System.currentTimeMillis().toString()
    fun addFormElements() {
        mStepName = view?.argument?.getString("stepName")
        val step: JSONObject? = view?.getStep(mStepName)
        try {
            mStepDetails = JSONObject(step.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val views: List<View> = mJsonFormInteractor.fetchFormElements(
            mStepName, view?.appContext, mStepDetails!!,
            view?.commonListener
        )
        view?.addFormElements(views)
    }

    @SuppressLint("ResourceAsColor")
    fun setUpToolBar() {
        if (mStepName != JsonFormConstants.FIRST_STEP_NAME) {
            view?.setUpBackButton()
        }
        view?.setActionBarTitle(mStepDetails!!.optString("title"))
        if (mStepDetails!!.has("next")) {
            view?.updateVisibilityOfNextAndSave(true, false)
        } else {
            view?.updateVisibilityOfNextAndSave(false, true)
        }
        view?.setToolbarTitleColor(R.color.white)
    }

    fun onBackClick() {
        view?.hideKeyBoard()
        view?.backClick()
    }

    fun onNextClick(mainView: LinearLayout) {
        val validationStatus: ValidationStatus = writeValuesAndValidate(mainView)
        if (validationStatus.isValid()) {
            val next: JsonFormFragment =
                JsonFormFragment.getFormFragment(mStepDetails!!.optString("next"))
            view?.hideKeyBoard()
            view?.transactThis(next)
        } else {
            view?.showToast(validationStatus.getErrorMessage())
        }
    }

    fun writeValuesAndValidate(mainview: LinearLayout): ValidationStatus {
        val childCount: Int? = mainview?.getChildCount()
        for (i in 0 until childCount!!) {
            val childAt: View? = mainview?.getChildAt(i)
            val key = childAt?.getTag(R.id.key) as String
            if (childAt is LinearLayout) {
                if (childAt?.getTag(R.id.type) == JsonFormConstants.EDIT_TEXT) {
                    val error_message: TextView =
                        childAt?.findViewById<View>(R.id.error_message) as TextView
                    val mLay: LinearLayout = childAt as LinearLayout

                }
            }
            else if (childAt is CheckBox) {
                val parentKey = childAt?.getTag(R.id.key) as String
                val childKey = childAt?.getTag(R.id.childKey) as String
                view?.writeValue(
                    mStepName,
                    parentKey,
                    JsonFormConstants.OPTIONS_FIELD_NAME,
                    childKey,
                    java.lang.String.valueOf((childAt as CheckBox).isChecked())
                )
            }
            else if (childAt is RadioButton) {
                val parentKey = childAt?.getTag(R.id.key) as String
                val childKey = childAt?.getTag(R.id.childKey) as String
                if ((childAt as RadioButton).isChecked()) {
                    view?.writeValue(mStepName, parentKey, childKey)
                }
            }
        }
        return ValidationStatus(true, null)
    }

    @Throws(JSONException::class)
    fun getFinalFormData(data: String?, userId: String?): String {
        var finalJson = ""
        if (data != null) {
            val serverArray = JSONArray()
            val jsonObject = JSONObject(data)
            val feildArray = jsonObject.getJSONObject("step1").getJSONArray("fields")
            for (x in 0 until feildArray.length()) {
                val serverObject = JSONObject()
                val mobj = feildArray.getJSONObject(x)
                val mid = mobj.getString("id")
                val mtype = mobj.getString("type")
                serverObject.put("id", mid)
                serverObject.put("type", mtype)
                if (mobj.has(JsonFormConstants.OPTIONS_FIELD_CHECK)) {
                    val options = mobj.getJSONArray(JsonFormConstants.OPTIONS_FIELD_CHECK)
                    val serverInnerArray = JSONArray()
                    for (i in 0 until options.length()) {
                        val moptionsObj = options.getJSONObject(i)
                        var mmid: String? = ""
                        if (moptionsObj.has("id")) {
                            mmid = moptionsObj.getString("id")
                        }
                        var mmval = ""
                        if (moptionsObj.has("val")) {
                            mmval = moptionsObj.getString("val")
                        }
                        if (moptionsObj.has("default")) {
                            mmval = moptionsObj.getString("default")
                            if (mtype.equals(JsonFormConstants.MULTI_CHECKBOX, ignoreCase = true)) {
                                if (!mmval.isEmpty() && !mmval.equals("", ignoreCase = true)) {
                                    val mjarray = moptionsObj.getJSONArray("default")
                                    val strarray = StringBuilder(mjarray.length())
                                    for (z in 0 until mjarray.length()) {
                                        val nArrastr = mjarray.getString(z)
                                        strarray.append("$nArrastr,")
                                    }
                                    mmval = strarray.toString()
                                }
                            }
                        }
                        val serverInnerObj = JSONObject()
                        serverInnerObj.put("id", mmid)
                        serverInnerObj.put("uid", userId)
                        serverInnerObj.put("tm", System.currentTimeMillis().toString())
                        serverInnerObj.put("val", mmval)
                        serverInnerArray.put(serverInnerObj)
                    }
                    serverObject.put("fields", serverInnerArray)
                    serverArray.put(serverObject)
                } else {
                    if (mobj.has("options")) {
                        val options = mobj.getJSONArray("options")
                        val serverInnerArray = JSONArray()
                        for (i in 0 until options.length()) {
                            val moptionsObj = options.getJSONObject(i)
                            var mmid: String? = ""
                            if (moptionsObj.has("id")) {
                                mmid = moptionsObj.getString("id")
                            }
                            var mmval = ""
                            if (moptionsObj.has("defaultValue")) {
                                mmval = moptionsObj.getString("defaultValue")
                            }
                            if (moptionsObj.has("default")) {
                                mmval = moptionsObj.getString("default")
                            }
                            if (moptionsObj.has("val")) {
                                mmval = moptionsObj.getString("val")
                            }
                            if (mtype.equals(
                                    JsonFormConstants.EDIT_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.EMAIL_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.PASSWORD_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.NUMBER_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.DATE_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(JsonFormConstants.TEXT_AREA, ignoreCase = true)
                            ) {
                                if (moptionsObj.has("default")) {
                                    mmval = moptionsObj.getString("default")
                                }
                            }
                            val serverInnerObj = JSONObject()
                            serverInnerObj.put("id", mmid)
                            serverInnerObj.put("uid", userId)
                            serverInnerObj.put("tm", System.currentTimeMillis().toString())
                            if (mtype.equals(
                                    JsonFormConstants.MULTI_CHECKBOX,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.MULTI_RADIO,
                                    ignoreCase = true
                                ) || mtype.equals(JsonFormConstants.SPINNER, ignoreCase = true)
                            ) {
                                if (moptionsObj.has("fieldContent")) {
                                    val mfileval = moptionsObj.getString("fieldContent")
                                    serverInnerObj.put("val", mfileval)
                                }
                                if (mtype.equals(JsonFormConstants.SPINNER, ignoreCase = true)) {
                                    if (moptionsObj.has("default")) {
                                        mmval = moptionsObj.getString("default")
                                        if (mmval.equals("true", ignoreCase = true)) {
                                            serverInnerArray.put(serverInnerObj)
                                        }
                                    } else {
                                        serverInnerArray.put(serverInnerObj)
                                    }
                                } else {
                                    if (mmval.equals("true", ignoreCase = true)) {
                                        serverInnerArray.put(serverInnerObj)
                                    }
                                }
                            } else if (mtype.equals(
                                    JsonFormConstants.SIGN_VIEW,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.CUSTOM_IMAGE_VIEW,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.RECORD_AUDIO_VIEW,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.UPLOAD_AUDIO_VIEW,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.RECORD_VIDEO_VIEW,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.UPLOAD_VIDEO_VIEW,
                                    ignoreCase = true
                                )
                            ) {
                                if (moptionsObj.has("l_id")) {
                                    serverInnerObj.put("l_id", moptionsObj["l_id"])
                                    serverInnerArray.put(serverInnerObj)
                                } else if (moptionsObj.has("mediaId")) {
                                    serverInnerObj.put("mediaId", moptionsObj["mediaId"])
                                    serverInnerArray.put(serverInnerObj)
                                }
                            } else if (mtype.equals(
                                    JsonFormConstants.BARCODE_VIEW,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.EDIT_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.EMAIL_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.PASSWORD_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.NUMBER_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(
                                    JsonFormConstants.DATE_TEXT,
                                    ignoreCase = true
                                ) || mtype.equals(JsonFormConstants.TEXT_AREA, ignoreCase = true)
                            ) {
                                if (!mmval.isEmpty() || !mmval.equals("", ignoreCase = true)) {
                                    serverInnerObj.put("val", mmval)
                                    serverInnerArray.put(serverInnerObj)
                                }
                            } else {
                                serverInnerObj.put("val", mmval)
                                serverInnerArray.put(serverInnerObj)
                            }
                        }
                        serverObject.put("fields", serverInnerArray)
                        serverArray.put(serverObject)
                    } else {
                        serverArray.put(serverObject)
                    }
                }
            }

            // Log.d("actionJsonDataArray", serverArray.toString());
            // Log.d("actionJsonData", data);
            finalJson = serverArray.toString()
        }
        return finalJson
    }

    fun onClick(v: View) {
        val clearFocus = ClearFocus(true)
        val key = v.getTag(R.id.key) as String
        val type = v.getTag(R.id.type) as String
        if (JsonFormConstants.CHOOSE_IMAGE.equals(type)) {
            view?.hideKeyBoard()
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            mCurrentKey = key
            view?.startActivityForResult(galleryIntent, RESULT_LOAD_IMG)
        }
        else if (JsonFormConstants.GPS_VIEW?.equals(type) == true) {
            //GPS View Code
        }
        else {
            var childId = 1
            val media_recyclerview_list: RecyclerView = v.getTag(R.id.element) as RecyclerView
            if (media_recyclerview_list != null) {
                childId = if (media_recyclerview_list.getAdapter() != null) {
                    media_recyclerview_list.adapter?.getItemCount()?:0
                }
                    else
                {
                    0
                }
            }
            val mediaId = v.getTag(R.id.mediaId) as String
            val elementId = v.getTag(R.id.key) as String + "_" + (childId + 1)
            val alias = System.currentTimeMillis().toString()
            media_recyclerview_list.setTag(R.id.alias, alias)
            if (media_recyclerview_list.getTag(R.id.mEdit) != null) {
                val mRecyclerAlias: String = media_recyclerview_list.getTag(R.id.mEdit).toString()
                try {
                    val mJobj = JSONObject(mRecyclerAlias)
                    if (mJobj.getString("l_id") == null || mJobj.getString("l_id").toString()
                            .isEmpty() || mJobj.getString("l_id").toString()
                            .equals("", ignoreCase = true)
                    ) {
                        val mDlid = currentTimesapn
                        val mDid = mJobj.getString("id")
                        media_recyclerview_list.setTag(R.id.mdata, mDlid)
                    } else {
                        val mDlid = mJobj.getString("l_id")
                        val mDid = mJobj.getString("id")
                        media_recyclerview_list.setTag(R.id.mdata, mDlid)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //  media_recyclerview_list.setTag(R.id.mdata, currentTimesapn);
                }
            } else {
                media_recyclerview_list.setTag(R.id.mdata, currentTimesapn)
            }
        }
    }

    fun formateDate(str: String?): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var date: Date? = null
        try {
            date = dateFormat.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val postFormater =
            SimpleDateFormat("dd MMMM yyyy - HH:mm aa")
        return postFormater.format(date)
    }

       fun onCheckedChanged(compoundButton: CompoundButton, isChecked: Boolean) {
        if (compoundButton is CheckBox) {
            val parentKey = compoundButton.getTag(R.id.key) as String
            val mparentKey = compoundButton.getTag(R.id.pkey) as String
            val childKey = compoundButton.getTag(R.id.childKey) as String
            view?.writeValueMultiCheck(
                mStepName,
                parentKey,
                mparentKey,
                JsonFormConstants.OPTIONS_FIELD_CHECK,
                childKey,
                childKey
            )
        } else if (compoundButton is RadioButton) {
            if (isChecked) {
                val parentKey = compoundButton.getTag(R.id.key) as String
                val mparentKey = compoundButton.getTag(R.id.pkey) as String
                val childKey = compoundButton.getTag(R.id.childKey) as String
                view?.unCheckAllExcept(parentKey, childKey, compoundButton as RadioButton)
                view?.writeValueMultiCheck(
                    mStepName,
                    parentKey,
                    mparentKey,
                    JsonFormConstants.OPTIONS_FIELD_CHECK,
                    childKey,
                    childKey
                )
            }
        }
    }


    companion object {
        private const val TAG = "FormFragmentPresenter"
        private const val RESULT_LOAD_IMG = 1
        val aPIVerison: Float
            get() {
                var f: Float? = null
                try {
                    val strBuild = StringBuilder()
                    strBuild.append(Build.VERSION.RELEASE.substring(0, 2))
                    f = strBuild.toString().toFloat()
                } catch (e: NumberFormatException) {
                    Log.e("", "error retriving api version" + e.message)
                }
                return f!!.toFloat()
            }
    }
}