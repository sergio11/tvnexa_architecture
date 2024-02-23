package com.dreamsoftware.api.rest.routes

import com.dreamsoftware.api.rest.utils.doIfParamExists
import com.dreamsoftware.api.rest.utils.generateSuccessResponse
import com.dreamsoftware.api.rest.controllers.ICategoryController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Class representing the routes related to categories in the application.
 * These routes include functionalities such as retrieving all categories and finding a category by ID.
 *
 * @property categoryController An instance of the [ICategoryController] interface for handling category-related operations.
 */
fun Route.categoriesRoutes() {

    val categoryController by inject<ICategoryController>()

    /**
     * Defines the routes under the "/categories" endpoint for category-related operations.
     */
    route("/categories") {

        /**
         * Endpoint for retrieving all categories.
         * Accepts GET requests to "/categories/" and retrieves all categories using the [categoryController.findAll] method.
         * Generates a success response with a code of 1001, a message indicating successful retrieval of categories,
         * and data containing the list of categories.
         */
        get("/") {
            call.generateSuccessResponse(
                code = 1001,
                message = "Categories retrieved successfully.",
                data = categoryController.findAll()
            )
        }

        /**
         * Endpoint for finding a category by ID.
         * Accepts GET requests to "/categories/{categoryId}" and retrieves a category by its ID
         * using the [categoryController.findById] method.
         * Generates a success response with a code of 1002, a message indicating successful category retrieval,
         * and data containing the details of the found category.
         *
         * @param categoryId The ID of the category to be retrieved.
         */
        get("/{categoryId}") {
            with(call) {
                doIfParamExists("categoryId") { categoryId ->
                    generateSuccessResponse(
                        code = 1002,
                        message = "Category found.",
                        data = categoryController.findById(categoryId)
                    )
                }
            }
        }
    }
}