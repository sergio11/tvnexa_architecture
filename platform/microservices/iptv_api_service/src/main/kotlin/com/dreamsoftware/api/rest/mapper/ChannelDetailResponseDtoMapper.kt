package com.dreamsoftware.api.rest.mapper

import com.dreamsoftware.api.rest.dto.*
import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper class that maps [ChannelDetailEntity] objects to [ChannelDetailResponseDTO] objects.
 *
 * @property countryMapper The mapper for mapping [CountryEntity] objects to [CountryResponseDTO] objects.
 * @property subdivisionMapper The mapper for mapping [SubdivisionEntity] objects to [SubdivisionResponseDTO] objects.
 * @property languageMapper The mapper for mapping [LanguageEntity] objects to [LanguageResponseDTO] objects.
 * @property categoryMapper The mapper for mapping [CategoryEntity] objects to [CategoryResponseDTO] objects.
 * @property channelStreamMapper The mapper for mapping [ChannelStreamEntity] objects to [ChannelStreamResponseDTO] objects.
 */
class ChannelDetailResponseDtoMapper(
    private val countryMapper: ISimpleMapper<CountryEntity, CountryResponseDTO>,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntity, SubdivisionResponseDTO>,
    private val languageMapper: ISimpleMapper<LanguageEntity, LanguageResponseDTO>,
    private val categoryMapper: ISimpleMapper<CategoryEntity, CategoryResponseDTO>,
    private val channelStreamMapper: ISimpleMapper<ChannelStreamEntity, ChannelStreamResponseDTO>
) : ISimpleMapper<ChannelDetailEntity, ChannelDetailResponseDTO> {

    /**
     * Map a single [ChannelDetailEntity] object to a [ChannelDetailResponseDTO] object.
     *
     * @param input The [ChannelDetailEntity] object to be mapped.
     * @return A [ChannelDetailResponseDTO] object representing the mapped data.
     */
    override fun map(input: ChannelDetailEntity): ChannelDetailResponseDTO {
        val countryDto = countryMapper.map(input.country)
        val subdivisionDto = input.subdivision?.let { subdivisionMapper.map(it) }
        val languagesDto = languageMapper.mapList(input.languages)
        val categoriesDto = categoryMapper.mapList(input.categories)
        return ChannelDetailResponseDTO(
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
            languages = languagesDto.toList(),
            streams = channelStreamMapper.mapList(input.streams).toList(),
            categories = categoriesDto.toList(),
            altNames = input.altNames,
            owners = input.owners,
            broadcastAreas = input.broadcastAreas
        )
    }

    /**
     * Map a collection of [ChannelDetailEntity] objects to a collection of [ChannelDetailResponseDTO] objects.
     *
     * @param input The collection of [ChannelDetailEntity] objects to be mapped.
     * @return A collection of [ChannelDetailResponseDTO] objects representing the mapped data.
     */
    override fun mapList(input: Iterable<ChannelDetailEntity>): Iterable<ChannelDetailResponseDTO> =
        input.map(::map)
}