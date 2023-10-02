package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*

/**
 * Mapper for converting [ChannelEntityDAO] objects to [ChannelEntity] objects.
 *
 * This mapper is responsible for mapping the data from the database representation ([ChannelEntityDAO])
 * to the domain representation ([ChannelEntity]) of a channel.
 *
 * @param countryMapper Mapper for converting [CountryEntityDAO] to [CountryEntity].
 * @param subdivisionMapper Mapper for converting [SubdivisionEntityDAO] to [SubdivisionEntity].
 * @param languageMapper Mapper for converting [LanguageEntityDAO] to [LanguageEntity].
 * @param categoryMapper Mapper for converting [CategoryEntityDAO] to [CategoryEntity].
 * @param streamMapper Mapper for converting [ChannelStreamEntityDAO] to [ChannelStreamEntity].
 */
class ChannelEntityDaoSimpleMapper(
    private val countryMapper: ISimpleMapper<CountryEntityDAO, CountryEntity>,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntityDAO, SubdivisionEntity>,
    private val languageMapper: ISimpleMapper<LanguageEntityDAO, LanguageEntity>,
    private val categoryMapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity>,
    private val streamMapper: ISimpleMapper<ChannelStreamEntityDAO, ChannelStreamEntity>
) : ISimpleMapper<ChannelEntityDAO, ChannelEntity> {

    /**
     * Maps a single [ChannelEntityDAO] object to a [ChannelEntity] object.
     *
     * @param input The input [ChannelEntityDAO] to map.
     * @return The mapped [ChannelEntity].
     */
    override fun map(input: ChannelEntityDAO): ChannelEntity = with(input) {
        ChannelEntity(
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
            replacedBy = replacedBy?.let(::map),
            languages = languageMapper.mapList(languages),
            categories = categoryMapper.mapList(categories),
            stream = stream?.let(streamMapper::map)
        )
    }

    /**
     * Maps a list of [ChannelEntityDAO] objects to a list of [ChannelEntity] objects.
     *
     * @param input The input list of [ChannelEntityDAO] objects to map.
     * @return The mapped list of [ChannelEntity] objects.
     */
    override fun mapList(input: Iterable<ChannelEntityDAO>): Iterable<ChannelEntity> =
        input.map(::map)
}