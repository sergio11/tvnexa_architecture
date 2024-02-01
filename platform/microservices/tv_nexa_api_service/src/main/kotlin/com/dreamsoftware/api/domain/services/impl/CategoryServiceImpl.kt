package com.dreamsoftware.api.domain.services.impl

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.domain.repository.ICategoryRepository
import com.dreamsoftware.api.domain.services.ICategoryService
import com.dreamsoftware.api.domain.services.impl.core.SupportService
import com.dreamsoftware.api.rest.dto.response.CategoryResponseDTO
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CategoryEntity

/**
 * Implementation of [ICategoryService] responsible for handling category-related operations.
 *
 * @property categoryRepository The repository handling data access for categories.
 * @property mapper The mapper responsible for converting entities to DTOs.
 */
internal class CategoryServiceImpl(
    private val categoryRepository: ICategoryRepository,
    private val mapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>
): SupportService(), ICategoryService {

    /**
     * Retrieves all categories.
     *
     * @return A list of [CategoryResponseDTO] representing all categories.
     * @throws AppException.InternalServerError If an error occurs during the retrieval process.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<CategoryResponseDTO> =
        safeCall(errorMessage = "An error occurred while fetching all categories.") {
            categoryRepository.findAll().map(mapper::map)
        }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return A [CategoryResponseDTO] representing the category.
     * @throws AppException.InternalServerError If an error occurs during the retrieval process.
     * @throws AppException.NotFoundException.CategoryNotFoundException If the category with the given ID is not found.
     */
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.CategoryNotFoundException::class
    )
    override suspend fun findById(id: String): CategoryResponseDTO =
        safeCall(errorMessage = "An error occurred while finding category by ID.") {
            categoryRepository.findById(id)?.let(mapper::map) ?:
                throw AppException.NotFoundException.CategoryNotFoundException("Category with ID '$id' not found.")
        }
}