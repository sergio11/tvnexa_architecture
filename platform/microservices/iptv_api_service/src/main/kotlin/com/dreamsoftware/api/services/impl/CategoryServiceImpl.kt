package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.CategoryResponseDTO
import com.dreamsoftware.api.repository.ICategoryRepository
import com.dreamsoftware.api.services.ICategoryService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

/**
 * Implementation of [ICategoryService] responsible for handling category-related operations.
 *
 * @property categoryRepository The repository handling data access for categories.
 * @property mapper The mapper responsible for converting entities to DTOs.
 */
class CategoryServiceImpl(
    private val categoryRepository: ICategoryRepository,
    private val mapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>
): ICategoryService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Retrieves all categories.
     *
     * @return A list of [CategoryResponseDTO] representing all categories.
     * @throws AppException.InternalServerError If an error occurs during the retrieval process.
     */
    @Throws(AppException.InternalServerError::class)
    override suspend fun findAll(): List<CategoryResponseDTO> = withContext(Dispatchers.IO) {
        try {
            categoryRepository.findAll().map(mapper::map)
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("CS (findAll) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while fetching all categories.")
        }
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
    override suspend fun findById(id: String): CategoryResponseDTO = withContext(Dispatchers.IO) {
        try {
            categoryRepository.findById(id)?.let(mapper::map) ?:
                throw AppException.NotFoundException.CategoryNotFoundException("Category with ID '$id' not found.")
        } catch (e: Exception) {
            e.printStackTrace()
            log.debug("CS (findById) An exception occurred: ${e.message ?: "Unknown error"}")
            throw AppException.InternalServerError("An error occurred while finding category by ID.")
        }
    }
}