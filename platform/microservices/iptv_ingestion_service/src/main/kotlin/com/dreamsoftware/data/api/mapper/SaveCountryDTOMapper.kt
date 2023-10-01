package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveCountryEntity
import com.dreamsoftware.data.iptvorg.model.CountryDTO

/**
 * Mapper class for converting [CountryDTO] objects to [SaveCountryEntity] objects.
 * This mapper is responsible for transforming data transfer objects (DTOs) received from
 * a network source into entities that can be stored in a database.
 */
class SaveCountryDTOMapper : ISimpleMapper<CountryDTO, SaveCountryEntity> {

    /**
     * Maps a single [CountryDTO] object to a [SaveCountryEntity] object.
     *
     * @param input The input [CountryDTO] object to be mapped.
     * @return A [SaveCountryEntity] object containing the mapped data.
     */
    override fun map(input: CountryDTO): SaveCountryEntity = with(input) {
        // Create a new SaveCountryEntity object using properties from CountryDTO.
        SaveCountryEntity(
            code = code,
            name = name,
            flag = flag,
            languages = languages
        )
    }

    /**
     * Maps a collection of [CountryDTO] objects to a collection of [SaveCountryEntity] objects.
     *
     * @param input The iterable collection of [CountryDTO] objects to be mapped.
     * @return An iterable collection of [SaveCountryEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<CountryDTO>): Iterable<SaveCountryEntity> =
        input.map(::map)
}
