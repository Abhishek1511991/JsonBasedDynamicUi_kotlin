package com.demo.dynamicjsonui.dynamicui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.demo.dynamicjsonui.MainActivity
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.font_changer.FontChanger
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.JsonApi
import com.demo.dynamicjsonui.dynamicui.mvp.MvpFragment
import com.demo.dynamicjsonui.dynamicui.presenter.JsonFormFragmentPresenter
import com.demo.dynamicjsonui.dynamicui.viewState.JsonFormFragmentViewState
import com.demo.dynamicjsonui.dynamicui.views.JsonFormFragmentView
import org.json.JSONException
import org.json.JSONObject

class JsonFormFragment: MvpFragment<JsonFormFragmentPresenter, JsonFormFragmentViewState>(),
    CommonListener, JsonFormFragmentView<JsonFormFragmentViewState> {

    private var mMainView: LinearLayout? = null
    private var mMenu: Menu? = null
    private var mJsonApi: JsonApi? = null

   companion object{
       fun getFormFragment(stepName: String?): JsonFormFragment {
           val jsonFormFragment = JsonFormFragment()
           val bundle = Bundle()
           bundle.putString("stepName", stepName)
           jsonFormFragment.arguments= bundle
           return jsonFormFragment
       }
   }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_json_wizard, null)
        mMainView = rootView.findViewById<View>(R.id.main_layout) as LinearLayout

        return rootView
    }

    override fun onAttach(context: Context) {
        mJsonApi = activity as JsonApi
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.addFormElements()
    }

    override fun dynamicResponser(formId: String?) {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        mMenu = menu
        menu.clear()
        // inflater.inflate(R.menu.menu_toolbar, menu);
        // inflater.inflate(R.menu.menu_toolbar, menu);
        presenter?.setUpToolBar()
    }

    override fun onClick(v: View?) {
        v?.let { presenter?.onClick(it) }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView != null) {
            presenter!!.onCheckedChanged(buttonView, isChecked)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun updateRelevantImageView(bitmap: Bitmap?, imagePath: String?, currentKey: String?) {
        val childCount = mMainView!!.childCount
        for (i in 0 until childCount) {
            val view = mMainView!!.getChildAt(i)
            if (view is ImageView) {
                val imageView = view
                val key = imageView.getTag(R.id.key) as String
                if (key == currentKey) {
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = View.VISIBLE
                    imageView.setTag(R.id.imagePath, imagePath)
                }
            }
        }
    }

    override fun writeValue(stepName: String?, key: String?, value: String?) {
        try {
            mJsonApi!!.writeValue(stepName, key, value)
        } catch (e: JSONException) {
            // TODO - handle
            e.printStackTrace()
        }
    }

    override fun writeValue(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        try {
            mJsonApi!!.writeValue(stepName, prentKey, childObjectKey, childKey, value)
        } catch (e: JSONException) {
            // TODO - handle
            e.printStackTrace()
        }
    }

    override fun writeValueMulti(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        try {
            mJsonApi!!.writeValueMulti(stepName, prentKey, childObjectKey, childKey, value)
        } catch (e: JSONException) {
            // TODO - handle
            e.printStackTrace()
        }
    }

    override fun writeValueMultiChange(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        try {
            mJsonApi!!.writeValueMultiChange(stepName, prentKey, childObjectKey, childKey, value)
        } catch (e: JSONException) {
            // TODO - handle
            e.printStackTrace()
        }
    }

    override fun writeValueMultiCheck(
        stepName: String?,
        prentKey: String?,
        mprentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    ) {
        try {
            mJsonApi!!.writeValueMultiCheck(
                stepName,
                prentKey,
                mprentKey,
                childObjectKey,
                childKey,
                value
            )
        } catch (e: JSONException) {
            // TODO - handle
            e.printStackTrace()
        }
    }

    override fun getStep(stepName: String?): JSONObject? {
        return mJsonApi?.getStep(stepName)
    }

    override fun createPresenter(): JsonFormFragmentPresenter {
        return JsonFormFragmentPresenter()
    }

    override fun addFormElements(views: List<View?>?) {
        for (view in views!!) {
            mMainView!!.addView(view)
        }

        mMainView?.let { FontChanger(requireContext()).changeFont(it) }
    }

    override fun unCheckAllExcept(
        parentKey: String?,
        childKey: String?,
        radioButton: RadioButton?
    ) {
        val multi_container = radioButton!!.parent as LinearLayout
        val childCount = multi_container.childCount

        for (i in 0 until childCount) {
            val view = multi_container.getChildAt(i)
            if (view is RadioButton) {
                val radio = view
                val parentKeyAtIndex = radio.getTag(R.id.key) as String
                val mparentKey = radio.getTag(R.id.pkey) as String
                val childKeyAtIndex = radio.getTag(R.id.childKey) as String
                if (parentKeyAtIndex == parentKey && childKeyAtIndex != childKey) {
                    radio.isChecked = false
                    // writeValueMultiCheck(JsonFormConstants.FIRST_STEP_NAME, parentKey,mparentKey, JsonFormConstants.OPTIONS_FIELD_CHECK, childKeyAtIndex, String.valueOf(false));
                }
            }
        }
    }


    override fun setActionBarTitle(title: String?) {
        TODO("Not yet implemented")
    }


    override fun showToast(message: String?) {
        TODO("Not yet implemented")
    }

    override val commonListener: CommonListener
        get() = this
    override val supportActionBar: ActionBar?
        get() = null
    override val toolbar: Toolbar?
        get() = null

    override fun setToolbarTitleColor(white: Int) {
    }

    override fun updateVisibilityOfNextAndSave(next: Boolean, save: Boolean) {
    }

    override fun hideKeyBoard() {
        (requireActivity() as MainActivity).hideKeyboard()
    }

    override fun transactThis(next: JsonFormFragment?) {

    }

    override val currentJsonState: String?
        get() =  mJsonApi?.currentJsonState()

    override fun finishWithResult(returnIntent: Intent?) {
        requireActivity().setResult(Activity.RESULT_OK, returnIntent)
        requireActivity().finish()
    }

    override fun setUpBackButton() {
    }

    override fun backClick() {
        requireActivity().onBackPressed()
    }

    override val count: String?
        get() = mJsonApi?.count
    override var argument: Bundle?
        get() = arguments
        set(value) {
            arguments= value
        }
    override val appContext: Context
        get() = requireContext()


}