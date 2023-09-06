package com.dreamsoftware.api.routes

import com.dreamsoftware.api.services.CategoryServiceException
import com.dreamsoftware.api.services.ICategoryService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

fun Routing.categoriesRoutes() {
    val categoryService by inject<ICategoryService>()

    route("/categories") {
        // Endpoint to retrieve all categories
        get("/") {
            handleCategoryExceptions {
                val categories = categoryService.findAll()
                call.respond(categories)
            }
        }

        // Endpoint to retrieve a category by its ID
        get("/{categoryId}") {
            val categoryId = call.parameters["categoryId"]
            if (categoryId != null) {
                handleCategoryExceptions {
                    val category = categoryService.findById(categoryId)
                    call.respond(category)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid category ID")
            }
        }
    }
}

// Custom exception handling function for categories
suspend fun PipelineContext<*, ApplicationCall>.handleCategoryExceptions(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: CategoryServiceException.InternalServerError) {
        // Internal server error exception for categories
        call.respond(HttpStatusCode.InternalServerError, "Internal server error: ${e.message}")
    } catch (e: CategoryServiceException.CategoryNotFoundException) {
        // Category not found exception
        call.respond(HttpStatusCode.NotFound, "Category not found: ${e.message}")
    }
}