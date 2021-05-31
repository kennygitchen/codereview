package com.myprojects.springnative.codereview.util.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.myprojects.springnative.codereview.api.request.ReviewSearchRequest
import java.beans.PropertyEditorSupport

class ReviewSearchRequestEditor(
    private val objectMapper: ObjectMapper
) : PropertyEditorSupport() {
    override fun setAsText(text: String?) {
        if (text.isNullOrEmpty()) throw IllegalArgumentException("ReviewSearchRequest is missing")
        else {
            try {
                value = objectMapper.readValue(text, ReviewSearchRequest::class.java)
            } catch (error: Exception) {
                throw IllegalArgumentException("Fail to parse ReviewSearchRequest", error)
            }
        }
    }
}