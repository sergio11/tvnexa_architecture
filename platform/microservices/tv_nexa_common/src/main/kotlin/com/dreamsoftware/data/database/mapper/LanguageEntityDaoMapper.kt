package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.dao.LanguageEntityDAO
import com.dreamsoftware.data.database.entity.LanguageEntity

/**
 * Mapper implementation to convert LanguageEntityDAO objects to LanguageEntity objects.
 */
class LanguageEntityDaoMapper : ISimpleMapper<LanguageEntityDAO, LanguageEntity> {

    /**
     * Maps a single LanguageEntityDAO object to a LanguageEntity object.
     *
     * @param input The LanguageEntityDAO object to be mapped.
     * @return A mapped LanguageEntity object.
     */
    override fun map(input: LanguageEntityDAO): LanguageEntity = with(input) {
        LanguageEntity(code, name)
    }

    /**
     * Maps a list of LanguageEntityDAO objects to a list of LanguageEntity objects.
     *
     * @param input An Iterable collection of LanguageEntityDAO objects to be mapped.
     * @return An Iterable collection of mapped LanguageEntity objects.
     */
    override fun mapList(input: Iterable<LanguageEntityDAO>): Iterable<LanguageEntity> =
        input.map(::map)
}