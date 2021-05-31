package com.myprojects.springnative.codereview.util.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.myprojects.springnative.codereview.api.request.ReviewSearchRequest
import spock.lang.Specification
import spock.lang.Unroll

class ReviewSearchRequestEditorTest extends Specification {

    private ObjectMapper objectMapper = new ObjectMapper()

    def 'setAsText() should set ReviewSearchRequest as value'() {
        given:
        def reviewerId = 1L
        def searchRequestStr = """{ "reviewerId":$reviewerId }"""
        def editor = new ReviewSearchRequestEditor(objectMapper)

        when:
        editor.setAsText(searchRequestStr)

        then:
        (editor.value as ReviewSearchRequest).reviewerId == reviewerId
    }

    @Unroll
    def 'setAsText() should throw exception - #_case_'() {
        given:
        def editor = new ReviewSearchRequestEditor(objectMapper)

        when:
        editor.setAsText(_search_req_str_)

        then:
        def error = thrown(IllegalArgumentException)
        error.message == _expected_

        where:
        _case_         | _search_req_str_        | _expected_
        'missing'      | ''                      | 'ReviewSearchRequest is missing'
        'wrong format' | '[ "reviewerId": "1" ]' | 'Fail to parse ReviewSearchRequest'
    }
}
