package com.dreamsoftware.api.services

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.CategoryResponseDTO

/**
 * Interface for managing categories.
 */
interface ICategoryService {

    /**
     * Retrieves all categories.
     *
     * @return Iterable of CategoryResponseDTO containing all categories.
     * @throws AppException.InternalServerError if there's an internal server error.
     */
    @Throws(AppException.InternalServerError::class)
    suspend fun findAll(): Iterable<CategoryResponseDTO>

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