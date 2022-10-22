package com.demo.dynamicjsonui.dynamicui.interfaces

import org.json.JSONException
import org.json.JSONObject
import kotlin.Throws

interface JsonApi {
    fun getStep(stepName: String?): JSONObject?

    @Throws(JSONException::class)
    fun writeValue(stepName: String?, key: String?, value: String?)

    @Throws(JSONException::class)
    fun writeValue(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    @Throws(JSONException::class)
    fun writeValueMulti(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    @Throws(JSONException::class)
    fun writeValueMultiChange(
        stepName: String?,
        prentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    @Throws(JSONException::class)
    fun writeValueMultiCheck(
        stepName: String?,
        prentKey: String?,
        mprentKey: String?,
        childObjectKey: String?,
        childKey: String?,
        value: String?
    )

    fun dynamicResponser(formId: String?)
    fun currentJsonState(): String?
    val count: String?
}