package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper for converting [ChannelEntityDAO] objects to [ChannelDetailEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([ChannelEntityDAO])
 * to the domain representation ([ChannelDetailEntity]) of a channel.
 *
 * @param countryMapper Mapper for converting [CountryEntityDAO] to [CountryEntity].
 * @param subdivisionMapper Mapper for converting [SubdivisionEntityDAO] to [SubdivisionEntity].
 * @param languageMapper Mapper for converting [LanguageEntityDAO] to [LanguageEntity].
 * @param categoryMapper Mapper for converting [CategoryEntityDAO] to [CategoryEntity].
 * @param streamMapper Mapper for converting [ChannelStreamEntityDAO] to [ChannelStreamEntity].
 */
class ChannelDetailEntityDaoMapper(
    private val countryMapper: ISimpleMapper<CountryEntityDAO, CountryEntity>,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntityDAO, SubdivisionEntity>,
    private val languageMapper: ISimpleMapper<LanguageEntityDAO, LanguageEntity>,
    private val categoryMapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity>,
    private val streamMapper: ISimpleMapper<ChannelStreamEntityDAO, ChannelStreamEntity>
) : ISimpleMapper<ChannelEntityDAO, ChannelDetailEntity> {

    /**
     * Maps a single [ChannelEntityDAO] object to a [ChannelDetailEntity] object.
     *
     * @param input The input [ChannelEntityDAO] to map.
     * @return The mapped [ChannelDetailEntity].
     */
    override fun map(input: ChannelEntityDAO): ChannelDetailEntity = with(input) {
        ChannelDetailEntity(
            channelId = channelId,
            name = name,
            network = network,
            country = country.let(countryMapper::map),
            subdivision = subdivision?.let(subdivisionMapper::map),
            city = city,
            isNsfw = isNsfw,
            website = website,
            logo = logo,
            launched = launched,
            closed = closed,
            catchupEnabled = catchupEnabled,
            replacedBy = replacedBy?.let(::map),
            languages = languageMapper.mapList(languages).toList(),
            categories = categoryMapper.mapList(categories).toList(),
            stream = null
        )
    }

    /**
     * Maps a list of [ChannelEntityDAO] objects to a list of [ChannelDetailEntity] objects.
     *
     * @param input The input list of [ChannelEntityDAO] objects to map.
     * @return The mapped list of [ChannelDetailEntity] objects.
     */
    override fun mapList(input: Iterable<ChannelEntityDAO>): Iterable<ChannelDetailEntity> =
        input.map(::map)
}