package com.dreamsoftware.api.mapper

import com.dreamsoftware.api.dto.*
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper class that maps [ChannelEntity] objects to [ChannelResponseDTO] objects.
 *
 * @property countryMapper The mapper for mapping [CountryEntity] objects to [CountryResponseDTO] objects.
 * @property subdivisionMapper The mapper for mapping [SubdivisionEntity] objects to [SubdivisionResponseDTO] objects.
 * @property languageMapper The mapper for mapping [LanguageEntity] objects to [LanguageResponseDTO] objects.
 * @property categoryMapper The mapper for mapping [CategoryEntity] objects to [CategoryResponseDTO] objects.
 * @property channelStreamMapper The mapper for mapping [ChannelStreamEntity] objects to [ChannelStreamResponseDTO] objects.
 */
class ChannelResponseDtoMapper(
    private val countryMapper: ISimpleMapper<CountryEntity, CountryResponseDTO>,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntity, SubdivisionResponseDTO>,
    private val languageMapper: ISimpleMapper<LanguageEntity, LanguageResponseDTO>,
    private val categoryMapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>,
    private val channelStreamMapper: ISimpleMapper<ChannelStreamEntity, ChannelStreamResponseDTO>
) : ISimpleMapper<ChannelEntity, ChannelResponseDTO> {

    /**
     * Map a single [ChannelEntity] object to a [ChannelResponseDTO] object.
     *
     * @param input The [ChannelEntity] object to be mapped.
     * @return A [ChannelResponseDTO] object representing the mapped data.
     */
    override fun map(input: ChannelEntity): ChannelResponseDTO {
        val countryDto = countryMapper.map(input.country)
        val subdivisionDto = input.subdivision?.let { subdivisionMapper.map(it) }
        val languagesDto = languageMapper.mapList(input.languages)
        val categoriesDto = categoryMapper.mapList(input.categories)
        val channelStreamDto = input.stream?.let { channelStreamMapper.map(it) }
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
            stream = channelStreamDto,
            categories = categoriesDto
        )
    }

    /**
     * Map a collection of [ChannelEntity] objects to a collection of [ChannelResponseDTO] objects.
     *
     * @param input The collection of [ChannelEntity] objects to be mapped.
     * @return A collection of [ChannelResponseDTO] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelEntity>): Iterable<ChannelResponseDTO> =
        input.map(::map)
}