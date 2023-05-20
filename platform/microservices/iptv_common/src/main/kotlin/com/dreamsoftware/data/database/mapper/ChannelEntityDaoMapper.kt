package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*

class ChannelEntityDaoMapper(
    private val countryMapper: IOneSideMapper<CountryEntityDAO, CountryEntity>,
    private val subdivisionMapper: IOneSideMapper<SubdivisionEntityDAO, SubdivisionEntity>,
    private val languageMapper: IOneSideMapper<LanguageEntityDAO, LanguageEntity>,
    private val categoryMapper: IOneSideMapper<CategoryEntityDAO, CategoryEntity>
): IOneSideMapper<ChannelEntityDAO, ChannelEntity> {

    override fun map(input: ChannelEntityDAO): ChannelEntity = with(input) {
        ChannelEntity(
            channelId = channelId,
            name = name,
            network = network,
            country = countryMapper.map(country),
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