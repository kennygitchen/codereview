package com.myprojects.springnative.codereview.core.validation

import com.myprojects.springnative.codereview.core.exception.ValidationFailedException
import spock.lang.Specification
import spock.lang.Unroll

class MobileValidatorTest extends Specification {

    @Unroll
    def 'check() should pass- #_case_'() {
        when:
        MobileValidator.@Companion.check(_mobile_)

        then:
        noExceptionThrown()

        where:
        _case_          | _mobile_
        'AU mobile'     | '0433333333'
        'International' | '+61433333333'

    }

    @Unroll
    def 'check() should fail - #_case_'() {
        when:
        MobileValidator.@Companion.check(_mobile_)

        then:
        thrown(ValidationFailedException)

        where:
        _case_      | _mobile_
        'empty'     | ''
        'too short' | '99999999'
        'too long'  | '9999999999999999'

    }
}
