package com.demo.dynamicjsonui.dynamicui.custom_views

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.CompoundButton
import androidx.annotation.NonNull
import com.rey.material.drawable.RippleDrawable
import com.rey.material.widget.RippleManager

open class CompoundButton : CompoundButton {
    private val mRippleManager = RippleManager()
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        // a fix to reset paddingLeft attribute
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val a = context.obtainStyledAttributes(
                attrs, intArrayOf(
                    android.R.attr.padding,
                    android.R.attr.paddingLeft
                ), defStyleAttr, defStyleRes
            )

            //if (!a.hasValue(0) && !a.hasValue(1))
            setPadding(0, paddingTop, paddingRight, paddingBottom)
            a.recycle()
        }
        isClickable = true
        applyStyle(context, attrs, defStyleAttr, defStyleRes)
    }

    open fun applyStyle(resId: Int) {
        applyStyle(context, null, 0, resId)
    }

    private fun applyStyle(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        mRippleManager.onCreate(this, context, attrs, defStyleAttr, defStyleRes)
    }

    override fun setBackgroundDrawable(drawable: Drawable) {
        val background = background
        if (background is RippleDrawable && drawable !is RippleDrawable) background.backgroundDrawable =
            drawable else super.setBackgroundDrawable(drawable)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (l === mRippleManager) super.setOnClickListener(l) else {
            mRippleManager.setOnClickListener(l)
            setOnClickListener(mRippleManager)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(@NonNull event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)
        return mRippleManager.onTouchEvent(this,event) || result
    }

    override fun setButtonDrawable(d: Drawable?) {
        mButtonDrawable = d
        super.setButtonDrawable(d)
    }

    override fun getCompoundPaddingLeft(): Int {
        var padding = super.getCompoundPaddingLeft()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) return padding
        if (mButtonDrawable != null) padding += mButtonDrawable!!.intrinsicWidth
        return padding
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        mButtonDrawable = right
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
    }
}