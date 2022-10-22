package com.demo.dynamicjsonui.dynamicui.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.CompoundButton
import com.rey.material.drawable.CheckBoxDrawable
import com.rey.material.widget.RippleManager

class CheckBox : CompoundButton {

    protected var mButtonDrawable: Drawable? = null


    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        applyStyle(context, attrs, defStyleAttr, defStyleRes)
    }

    fun applyStyle(resId: Int) {
        applyStyle(getContext(), null, 0, resId)
    }

    private fun applyStyle(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val drawable: CheckBoxDrawable = CheckBoxDrawable.Builder(context, attrs, defStyleAttr, defStyleRes).build()
        drawable.setInEditMode(isInEditMode())
        drawable.setAnimEnable(false)
        setButtonDrawable(null)
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        drawable.setAnimEnable(true)
    }

    /**
     * Change the checked state of this button immediately without showing
     * animation.
     *
     * @param checked
     * The checked state.
     */
    fun setCheckedImmediately(checked: Boolean) {
        if (mButtonDrawable is CheckBoxDrawable) {
            val drawable: CheckBoxDrawable = mButtonDrawable as CheckBoxDrawable
            drawable.setAnimEnable(false)
            setChecked(checked)
            drawable.setAnimEnable(true)
        } else setChecked(checked)
    }
}