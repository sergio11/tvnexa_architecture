package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.entity.*

class ChannelEntityDaoMapper(
    private val countryMapper: IMapper<CountryEntityDAO, CountryEntity>,
    private val subdivisionMapper: IMapper<SubdivisionEntityDAO, SubdivisionEntity>,
    private val languageMapper: IMapper<LanguageEntityDAO, LanguageEntity>,
    private val categoryMapper: IMapper<CategoryEntityDAO, CategoryEntity>
): IMapper<ChannelEntityDAO, ChannelEntity> {

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