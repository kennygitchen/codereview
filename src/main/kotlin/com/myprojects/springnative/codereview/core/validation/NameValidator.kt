package com.myprojects.springnative.codereview.core.validation

import com.myprojects.springnative.codereview.core.exception.ValidationFailedException

class NameValidator {
    companion object {
        private const val PATTERN = "[A-Za-z0-9.\'\\- ]{2,150}"
        fun check(value: String) {
            if (!value.matches(Regex(PATTERN)))
                throw ValidationFailedException("Invalid name")
        }
    }
}