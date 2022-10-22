package com.demo.dynamicjsonui.dynamicui.font_changer

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by abhishek on 29/12/16.
 */
class FontChanger(private val context: Context) {
    private var typeface: Typeface? = null
    fun changeFont(viewTree: ViewGroup) {
        typeface = Typeface.createFromAsset(context.assets, "fonts/lato-regular-webfont.ttf")
        var child: View?
        for (i in 0 until viewTree.childCount) {
            child = viewTree.getChildAt(i)
            if (child is ViewGroup) {
                // recursive call
                changeFont(child)
            } else if (child is TextView) {
                // base case
                child.typeface = typeface
            }
        }
    }
}