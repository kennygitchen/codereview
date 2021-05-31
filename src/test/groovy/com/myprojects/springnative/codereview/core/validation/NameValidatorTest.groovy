package com.myprojects.springnative.codereview.core.validation

import com.myprojects.springnative.codereview.core.exception.ValidationFailedException
import spock.lang.Specification
import spock.lang.Unroll

class NameValidatorTest extends Specification {

    @Unroll
    def 'check() should pass- #_case_'() {
        when:
        NameValidator.@Companion.check(_name_)

        then:
        noExceptionThrown()

        where:
        _case_                    | _name_
        'normal'                  | 'Michael Kim'
        'has allowed special "."' | 'St. Michael '
        'has allowed special "\'' | 'Michael O\'Donnal'
        'has allowed special "-"' | 'Michael-Allen'
        'has number'              | 'Michael 1'

    }

    @Unroll
    def 'check() should fail - #_case_'() {
        when:
        NameValidator.@Companion.check(_name_)

        then:
        thrown(ValidationFailedException)

        where:
        _case_                           | _name_
        'empty'                          | ''
        'less than 2 characters'         | 'a'
        'longer than 250 characters'     | ''
        'contains illegal character "!"' | 'M!chael'
        'contains illegal character "_'  | 'M!chael'
    }
}
