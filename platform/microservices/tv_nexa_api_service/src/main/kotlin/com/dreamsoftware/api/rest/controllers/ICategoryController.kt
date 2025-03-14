package com.dreamsoftware.api.rest.controllers

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.CategoryResponseDTO

/**
 * Interface for managing categories.
 */
interface ICategoryController {

    /**
     * Retrieves all categories.
     *
     * @return Iterable of CategoryResponseDTO containing all categories.
     * @throws AppException.InternalServerError if there's an internal server error.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): List<CategoryResponseDTO>

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return CategoryResponseDTO containing the category details.
     * @throws AppException.InternalServerError if there's an internal server error.
     * @throws AppException.NotFoundException.CategoryNotFoundException if the category is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.CategoryNotFoundException::class
    )
    suspend fun findById(id: String): CategoryResponseDTO
}