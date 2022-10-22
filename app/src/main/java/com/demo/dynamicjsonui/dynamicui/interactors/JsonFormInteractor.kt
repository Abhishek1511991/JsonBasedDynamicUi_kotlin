package com.demo.dynamicjsonui.dynamicui.interactors

import android.content.Context
import android.util.Log
import android.view.View
import com.demo.dynamicjsonui.dynamicui.constants.JsonFormConstants
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.widgets.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
class JsonFormInteractor private constructor() {
    private fun registerWidgets() {
        map[JsonFormConstants.RECORD_AUDIO_VIEW] = RecordAudioFactory()
        map[JsonFormConstants.RECORD_VIDEO_VIEW] = RecordVideoFactory()
        map[JsonFormConstants.UPLOAD_AUDIO_VIEW] = RecordAudioFactory()
        map[JsonFormConstants.UPLOAD_VIDEO_VIEW] = RecordVideoFactory()
        map[JsonFormConstants.MULTI_CHECKBOX] = MultiCheckBoxFactory()
        map[JsonFormConstants.MULTI_RADIO] = MultiRadioButtonFactory()
        map[JsonFormConstants.MULTI_SPINNER] = MultiSpinnerFactory()
        map[JsonFormConstants.PASSWORD_TEXT] = PasswordTextFactory()
        map[JsonFormConstants.CUSTOM_CARD_VIEW] = CardViewFactory()
        map[JsonFormConstants.CUSTOM_BUTTON_VIEW] = ButtonFactory()
        map[JsonFormConstants.CUSTOM_LINEAR_VIEW] = LinearFactory()
        map[JsonFormConstants.RADIO_BUTTON] = RadioButtonFactory()
        map[JsonFormConstants.CHOOSE_IMAGE] = ImagePickerFactory()
        map[JsonFormConstants.CUSTOM_IMAGE_VIEW] = ImageFactory()
        map[JsonFormConstants.NUMBER_TEXT] = NumberTextFactory()
        map[JsonFormConstants.LIST_VIEW] = RecylerViewFactory()
        map[JsonFormConstants.EMAIL_TEXT] = EmailTextFactory()
        map[JsonFormConstants.VIDEO_VIEW] = VideoViewFactory()
        map[JsonFormConstants.BARCODE_VIEW] = BarcodeFactory()
        map[JsonFormConstants.SIGN_VIEW] = SignatureFactory()
        map[JsonFormConstants.EDIT_TEXT] = EditTextFactory()
        map[JsonFormConstants.CHECK_BOX] = CheckBoxFactory()
        map[JsonFormConstants.DATE_TEXT] = DateTextFactory()
        map[JsonFormConstants.TEXT_AREA] = TextAreaFactory()
        map[JsonFormConstants.SPINNER] = SpinnerFactory()
        map[JsonFormConstants.GPS_VIEW] = GPSFactory()
        map[JsonFormConstants.LABEL] = LabelFactory()
    }

    fun fetchFormElements(
        stepName: String?,
        context: Context?,
        parentJson: JSONObject,
        listener: CommonListener?
    ): List<View> {
        Log.d(TAG, "fetchFormElements called")
        val viewsFromJson: MutableList<View> = ArrayList(5)
        try {
            val fields = parentJson.getJSONArray("fields")
            for (i in 0 until fields.length()) {
                val childJson = fields.getJSONObject(i)
                try {
                    val views: List<View?>? = map[childJson.getString("type")]?.getViewsFromJson(
                        map,
                        stepName,
                        context,
                        childJson,
                        listener
                    )
                    if (views?.size!! > 0) {
                        viewsFromJson.addAll(views as Collection<View>)
                    }
                } catch (e: Exception) {
                    Log.d(
                        TAG,
                        "Exception occurred in making child view at index : " + i + " : Exception is : "
                                + e.message
                    )
                    e.printStackTrace()
                }
            }
        } catch (e: JSONException) {
            Log.d(TAG, "Json exception occurred : " + e.message)
            e.printStackTrace()
        }
        return viewsFromJson
    }

    companion object {
        private const val TAG = "JsonFormInteractor"
        private val map: MutableMap<String, FormWidgetFactory> =
            HashMap<String, FormWidgetFactory>()
        val instance = JsonFormInteractor()
    }

    init {
        registerWidgets()
    }
}