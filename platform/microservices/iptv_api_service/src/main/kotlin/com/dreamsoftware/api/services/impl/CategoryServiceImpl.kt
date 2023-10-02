package com.dreamsoftware.api.services.impl

import com.dreamsoftware.api.dto.CategoryResponseDTO
import com.dreamsoftware.api.repository.ICategoryRepository
import com.dreamsoftware.api.services.CategoryServiceException
import com.dreamsoftware.api.services.ICategoryService
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryServiceImpl(
    private val categoryRepository: ICategoryRepository,
    private val mapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>
): ICategoryService {

    @Throws(CategoryServiceException.InternalServerError::class)
    override suspend fun findAll(): Iterable<CategoryResponseDTO> = withContext(Dispatchers.IO) {
        try {
            categoryRepository.findAll().map(mapper::map)
        } catch (e: Exception) {
            throw CategoryServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }

    @Throws(CategoryServiceException.InternalServerError::class, CategoryServiceException.CategoryNotFoundException::class)
    override suspend fun findById(id: String): CategoryResponseDTO = withContext(Dispatchers.IO) {
        try {
            val category = categoryRepository.findById(id)
            if (category != null) {
                mapper.map(category)
            } else {
                throw CategoryServiceException.CategoryNotFoundException(id)
            }
        } catch (e: Exception) {
            throw CategoryServiceException.InternalServerError(e.message ?: "Unknown error")
        }
    }
}