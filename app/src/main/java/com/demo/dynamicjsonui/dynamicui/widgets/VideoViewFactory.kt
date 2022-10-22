package com.demo.dynamicjsonui.dynamicui.widgets

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import com.demo.dynamicjsonui.R
import com.demo.dynamicjsonui.dynamicui.interfaces.CommonListener
import com.demo.dynamicjsonui.dynamicui.interfaces.FormWidgetFactory
import com.demo.dynamicjsonui.dynamicui.utils.FormUtils
import com.demo.dynamicjsonui.dynamicui.utils.ValidationStatus
import org.json.JSONObject

/**
 * Created by Abhishek at 20/10/2022
 */
class VideoViewFactory : FormWidgetFactory {



    companion object {
        fun validate(videoView: VideoView): ValidationStatus {
            if (videoView.getTag(R.id.v_required) !is String || videoView.getTag(R.id.error) !is String) {
                return ValidationStatus(true, null)
            }
            val isRequired = videoView.getTag(R.id.v_required) as Boolean
            if (!isRequired) {
                return ValidationStatus(true, null)
            }
            val path: Any = videoView.getTag(R.id.key)
            return if (path is String && !TextUtils.isEmpty(path)) {
                ValidationStatus(true, null)
            } else ValidationStatus(false, videoView.getTag(R.id.error) as String)
        }
    }

    override fun getViewsFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): List<View?>{
        val views: MutableList<View> = ArrayList(1)
        Log.d("vide view", "test")
        val video = VideoView(context)
        video.setMediaController(MediaController(context))
        video.setLayoutParams(
            FormUtils.getLayoutParams(
                FormUtils.MATCH_PARENT,
                FormUtils.dpToPixels(context!!, 200F),
                0,
                0,
                0,
                0
            )
        )
        video.setTag(R.id.key, jsonObject?.getString("url"))
        video.setTag(R.id.type, jsonObject?.getString("type"))
        video.setVideoURI(Uri.parse(jsonObject?.getString("url")))
        video.requestFocus()

        video.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer) {
                (context as Activity).runOnUiThread(Runnable {
                    mp.setLooping(true)
                    video.start()
                })
            }
        })
        views.add(video)

        return views
    }

    override fun getViewFromJson(
        map: MutableMap<String, FormWidgetFactory>,
        stepName: String?,
        context: Context?,
        jsonObject: JSONObject?,
        listener: CommonListener?
    ): View {
        val video = VideoView(context)
        video.setMediaController(MediaController(context))
        video.setLayoutParams(
            FormUtils.getLayoutParams(
                FormUtils.MATCH_PARENT,
                FormUtils.dpToPixels(context!!, 200F),
                0,
                0,
                0,
                0
            )
        )
        video.setTag(R.id.key, jsonObject?.getString("url"))
        video.setTag(R.id.type, jsonObject?.getString("type"))
        video.setVideoURI(Uri.parse(jsonObject?.getString("url")))
        video.requestFocus()

        video.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer) {
                (context as Activity).runOnUiThread(Runnable {
                    mp.setLooping(true)
                    video.start()
                })
            }
        })

        return video
    }
}