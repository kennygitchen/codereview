package com.myprojects.springnative.codereview.core.validation

import com.myprojects.springnative.codereview.core.exception.ValidationFailedException

class MobileValidator {
    companion object {
        private const val PATTERN = "((^[\\+])?([0-9])){10,15}"
        fun check(value: String) {
            if (!value.matches(Regex(PATTERN)))
                throw ValidationFailedException("Invalid mobile")
        }
    }
}