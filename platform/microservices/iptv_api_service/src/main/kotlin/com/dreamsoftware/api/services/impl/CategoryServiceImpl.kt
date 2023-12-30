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

class CategoryServiceImpl(
    private val categoryRepository: ICategoryRepository,
    private val mapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>
): ICategoryService {

    private val log = LoggerFactory.getLogger(this::class.java)

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