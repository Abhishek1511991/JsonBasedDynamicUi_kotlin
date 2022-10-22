package com.demo.dynamicjsonui.dynamicui.custom_views

import android.content.Context
import android.util.AttributeSet

import com.rey.material.drawable.RadioButtonDrawable

/**
 * Created by Abhishek at 03/10/2022
 */
class RadioButton : CompoundButton {
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

    override fun applyStyle(resId: Int) {
        applyStyle(getContext(), null, 0, resId)
    }

    private fun applyStyle(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val drawable = RadioButtonDrawable.Builder(context, attrs, defStyleAttr, defStyleRes)
            .build()
        drawable.setInEditMode(isInEditMode())
        drawable.isAnimEnable = false
        setButtonDrawable(null)
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        drawable.isAnimEnable = true
    }

    override fun toggle() {
        // we override to prevent toggle when the radio is already
        // checked (as opposed to check boxes widgets)
        if (!isChecked()) {
            super.toggle()
        }
    }

    /**
     * Change the checked state of this button immediately without showing
     * animation.
     *
     * @param checked
     * The checked state.
     */
    fun setCheckedImmediately(checked: Boolean) {
        if (mButtonDrawable is RadioButtonDrawable) {
            val drawable = mButtonDrawable as RadioButtonDrawable
            drawable.isAnimEnable = false
            setChecked(checked)
            drawable.isAnimEnable = true
        } else setChecked(checked)
    }
}