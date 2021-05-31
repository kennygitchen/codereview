package com.myprojects.springnative.codereview.core.validation

import com.myprojects.springnative.codereview.core.exception.ValidationFailedException
import spock.lang.Specification
import spock.lang.Unroll

class EmailValidatorTest extends Specification {

    @Unroll
    def 'check() should pass- #_case_'() {
        when:
        EmailValidator.@Companion.check(_email_)

        then:
        noExceptionThrown()

        where:
        _case_    | _email_
        'valid 1' | 'test@gmail.com'
        'valid 2' | 'test.test@gmail.com.au'
        'valid 3' | 'test1.test1@gmail.com.au'

    }

    @Unroll
    def 'check() should fail - #_case_'() {
        when:
        EmailValidator.@Companion.check(_mobile_)

        then:
        thrown(ValidationFailedException)

        where:
        _case_                | _mobile_
        'empty'               | ''
        'without @'           | '99999999'
        'without domain'      | 'test@'
        'with invalid domain' | 'test@gmail'

    }
}
