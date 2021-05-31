package com.myprojects.springnative.codereview.api

import com.myprojects.springnative.codereview.core.dao.JpaCriteriaDAO
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckApi @Autowired constructor(
    private val jpaCriteriaDAO: JpaCriteriaDAO
) {

    @GetMapping("/healthcheck")
    fun health(): String {
        // ensure database is healthy
        try {
            jpaCriteriaDAO.findAll().map { criteria -> criteria }.toList()
        } catch (error: Exception) {
            LOGGER.error("Database connection failed.", error)
            return "Database connection failed."
        }
        return "I am healthy"
    }

    companion object {
        val LOGGER = LogManager.getLogger()
    }
}