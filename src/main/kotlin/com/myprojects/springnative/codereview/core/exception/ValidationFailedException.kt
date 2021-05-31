package com.myprojects.springnative.codereview.core.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class ValidationFailedException(
    override var message: String
) : RuntimeException(message)