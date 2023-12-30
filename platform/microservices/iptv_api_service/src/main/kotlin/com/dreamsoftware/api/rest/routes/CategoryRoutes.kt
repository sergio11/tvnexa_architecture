package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.model.ErrorType
import com.dreamsoftware.api.rest.utils.generateErrorResponse
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.services.ICategoryService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.categoriesRoutes() {
    val categoryService by inject<ICategoryService>()

    route("/categories") {
        // Endpoint to retrieve all categories
        get("/") {
            call.generateSuccessResponse(
                code = 1001,
                message = "Categories retrieved successfully.",
                data = categoryService.findAll()
            )
        }

        // Endpoint to retrieve a category by its ID
        get("/{categoryId}") {
            with(call) {
                parameters["categoryId"]?.let { categoryId ->
                    generateSuccessResponse(
                        code = 1002,
                        message = "Category found.",
                        data = categoryService.findById(categoryId)
                    )
                } ?: run {
                    generateErrorResponse(ErrorType.BAD_REQUEST)
                }
            }
        }
    }
}