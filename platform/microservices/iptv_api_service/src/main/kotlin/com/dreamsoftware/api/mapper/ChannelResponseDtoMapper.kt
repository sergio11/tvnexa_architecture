package com.dreamsoftware.api.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.api.dto.ChannelResponseDTO
import com.dreamsoftware.api.dto.CountryResponseDTO
import com.dreamsoftware.api.dto.SubdivisionResponseDTO
import com.dreamsoftware.api.dto.LanguageResponseDTO
import com.dreamsoftware.api.dto.CategoryResponseDTO
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.entity.LanguageEntity
import com.dreamsoftware.data.database.entity.CategoryEntity

class ChannelResponseDtoMapper(
    private val countryMapper: IMapper<CountryEntity, CountryResponseDTO>,
    private val subdivisionMapper: IMapper<SubdivisionEntity, SubdivisionResponseDTO>,
    private val languageMapper: IMapper<LanguageEntity, LanguageResponseDTO>,
    private val categoryMapper: IMapper<CategoryEntity, CategoryResponseDTO>
) : IMapper<ChannelEntity, ChannelResponseDTO> {

    override fun map(input: ChannelEntity): ChannelResponseDTO {
        val countryDto = countryMapper.map(input.country)
        val subdivisionDto = input.subdivision?.let { subdivisionMapper.map(it) }
        val languagesDto = languageMapper.mapList(input.languages)
        val categoriesDto = categoryMapper.mapList(input.categories)
        return ChannelResponseDTO(
            channelId = input.channelId,
            name = input.name,
            network = input.network,
            country = countryDto,
            subdivision = subdivisionDto,
            city = input.city,
            isNsfw = input.isNsfw,
            website = input.website,
            logo = input.logo,
            launched = input.launched,
            closed = input.closed,
            replacedBy = input.replacedBy?.let { map(it) },
            languages = languagesDto,
            categories = categoriesDto
        )
    }

    override fun mapList(input: Iterable<ChannelEntity>): Iterable<ChannelResponseDTO> =
        input.map(::map)
}