package com.dreamsoftware.api.services

import com.dreamsoftware.api.dto.CategoryResponseDTO

sealed class CategoryServiceException(message: String) : Exception(message) {
    class CategoryNotFoundException(id: String) : CategoryServiceException("Category with ID '$id' not found.")
    class InternalServerError(message: String) : CategoryServiceException("Internal server error: $message")
}

interface ICategoryService {
    @Throws(CategoryServiceException.InternalServerError::class)
    suspend fun findAll(): Iterable<CategoryResponseDTO>

    @Throws(CategoryServiceException.InternalServerError::class, CategoryServiceException.CategoryNotFoundException::class)
    suspend fun findById(id: String): CategoryResponseDTO
}