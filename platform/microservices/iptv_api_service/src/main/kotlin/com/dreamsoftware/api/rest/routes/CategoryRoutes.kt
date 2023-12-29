package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.services.ICategoryService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.categoriesRoutes() {
    val categoryService by inject<ICategoryService>()

    route("/categories") {
        // Endpoint to retrieve all categories
        get("/") {
            val categories = categoryService.findAll()
            call.respond(categories)
        }

        // Endpoint to retrieve a category by its ID
        get("/{categoryId}") {
            with(call) {
                val categoryId = parameters["categoryId"]
                if (categoryId != null) {
                    val category = categoryService.findById(categoryId)
                    respond(category)
                } else {
                    respond(HttpStatusCode.BadRequest, "Invalid category ID")
                }
            }
        }
    }
}