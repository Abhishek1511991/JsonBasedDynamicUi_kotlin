package com.demo.dynamicjsonui.dynamicui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache
import android.view.Display
import android.view.WindowManager


/**
 * Created by Abhishek at 03/10/2022
 */
object ImageUtils {
    private val mBitmapLruCache: LruCache<String, Bitmap> = LruCache(10000000)
    fun loadBitmapFromFile(path: String?, requiredWidth: Int, requiredHeight: Int): Bitmap? {
        val key = "$path:$requiredWidth:$requiredHeight"
        var bitmap: Bitmap = mBitmapLruCache.get(key)
        if (bitmap != null) {
            Log.d("ImagePickerFactory", "Found in cache.")
            return bitmap
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(path, options)
        mBitmapLruCache.put(key, bitmap)
        return bitmap
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height: Int = options.outHeight
        val width: Int = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight
                && halfWidth / inSampleSize > reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun getDeviceWidth(context: Context?): Int {
        val wm: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.getDefaultDisplay()
        return display.width
    }
}