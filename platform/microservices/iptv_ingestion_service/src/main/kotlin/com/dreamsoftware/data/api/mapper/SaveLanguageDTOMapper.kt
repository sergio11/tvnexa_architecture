package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveLanguageEntity
import com.dreamsoftware.data.iptvorg.model.LanguageDTO

/**
 * Mapper class for converting [LanguageDTO] objects to [SaveLanguageEntity] objects.
 * This mapper is responsible for transforming language data transfer objects (DTOs)
 * into entities that can be stored in a database.
 */
class SaveLanguageDTOMapper : ISimpleMapper<LanguageDTO, SaveLanguageEntity> {

    /**
     * Maps a single [LanguageDTO] object to a [SaveLanguageEntity] object.
     *
     * @param input The input [LanguageDTO] object to be mapped.
     * @return A [SaveLanguageEntity] object containing the mapped data.
     */
    override fun map(input: LanguageDTO): SaveLanguageEntity = with(input) {
        // Create a new SaveLanguageEntity object using properties from LanguageDTO.
        SaveLanguageEntity(code, name)
    }

    /**
     * Maps a collection of [LanguageDTO] objects to a collection of [SaveLanguageEntity] objects.
     *
     * @param input The iterable collection of [LanguageDTO] objects to be mapped.
     * @return An iterable collection of [SaveLanguageEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<LanguageDTO>): Iterable<SaveLanguageEntity> =
        input.map(::map)
}