package com.demo.dynamicjsonui.dynamicui.utils

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.demo.dynamicjsonui.R

/**
 * Created by Abhishek at 18/10/2022
 */
object FormUtils {

    const val FONT_BOLD_PATH = "fonts/diti_sweet.ttf"
    const val FONT_REGULAR_PATH = "fonts/robotoregular.ttf"

    const val MATCH_PARENT = -1
    const val WRAP_CONTENT = -2
    fun getLayoutParams(
        width: Int,
        height: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): LinearLayout.LayoutParams {
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height)
        layoutParams.setMargins(left, top, right, bottom)
        return layoutParams
    }

    fun getTextViewWith(
        context: Context?, textSizeInSp: Int, text: String?, key: String?, type: String?,
        layoutParams: LinearLayout.LayoutParams?, fontPath: String?
    ): TextView {
        val textView = TextView(context)
        textView.setText(text)
        textView.setTag(R.id.key, key)
        textView.setTag(R.id.type, type)
        textView.setId(ViewUtil.generateViewId())
        textView.setTextSize(textSizeInSp.toFloat())
        textView.setLayoutParams(layoutParams)
        textView.setTypeface(Typeface.createFromAsset(context?.assets, fontPath))
        return textView
    }

    fun dpToPixels(context: Context, dps: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dps * scale + 0.5f).toInt()
    }
}