package com.demo.dynamicjsonui.dynamicui.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.demo.dynamicjsonui.dynamicui.fragments.JsonFormFragment
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.viewState.ViewState
import org.json.JSONObject

/**
 * Created by Abhishek at 03/10/2022
 */
interface JsonFormFragmentView<VS : ViewState?> : MvpView {
    var argument: Bundle?
    fun setActionBarTitle(title: String?)
    val appContext: Context?
    fun showToast(message: String?)
    val commonListener: CommonListener?
    fun addFormElements(views: List<View?>?)
    val supportActionBar: ActionBar?
    val toolbar: Toolbar?
    fun setToolbarTitleColor(white: Int)
    fun updateVisibilityOfNextAndSave(next: Boolean, save: Boolean)
    fun hideKeyBoard()
    fun transactThis(next: JsonFormFragment?)
    fun startActivityForResult(intent: Intent?, requestCode: Int)
    fun updateRelevantImageView(bitmap: Bitmap?, imagePath: String?, currentKey: String?)
    fun writeValue(stepName: String?, key: String?, value: String?)
    fun writeValue(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    fun writeValueMulti(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    fun writeValueMultiChange(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    fun writeValueMultiCheck(
        stepName: String?,
        prentKey: String?,
        mprentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    fun getStep(stepName: String?): JSONObject?
    val currentJsonState: String?
    fun dynamicResponser(formId: String?)
    fun finishWithResult(returnIntent: Intent?)
    fun setUpBackButton()
    fun backClick()
    fun unCheckAllExcept(parentKey: String?, childKey: String?, radioButton: RadioButton?)
    val count: String?
}