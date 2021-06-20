package com.myprojects.springnative.codereview

import com.myprojects.springnative.codereview.api.response.*
import com.myprojects.springnative.codereview.core.service.ReviewService
import io.swagger.v3.oas.models.media.NumberSchema
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.nativex.hint.*
import org.springframework.web.bind.WebDataBinder
import java.time.LocalDateTime

@NativeHint(
        types = [
            TypeHint(types = [
                NumberSchema::class, WebDataBinder::class, ApplicantResponse::class, CriteriaResponse::class,
                ReviewerResponse::class, ReviewResponse::class, ScoreResponse::class
            ])
        ],
        serializables = [SerializationHint(types = [LocalDateTime::class])],
        aotProxies = [AotProxyHint(targetClass = ReviewService::class, proxyFeatures = ProxyBits.IS_STATIC)]
)
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
