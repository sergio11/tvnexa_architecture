package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*

class ChannelEntityDaoSimpleMapper(
    private val countryMapper: ISimpleMapper<CountryEntityDAO, CountryEntity>,
    private val subdivisionMapper: ISimpleMapper<SubdivisionEntityDAO, SubdivisionEntity>,
    private val languageMapper: ISimpleMapper<LanguageEntityDAO, LanguageEntity>,
    private val categoryMapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity>
): ISimpleMapper<ChannelEntityDAO, ChannelEntity> {

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
            categories = categoryMapper.mapList(categories)
        )
    }

    override fun mapList(input: Iterable<ChannelEntityDAO>): Iterable<ChannelEntity> =
        input.map(::map)
}