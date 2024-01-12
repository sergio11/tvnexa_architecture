package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.iptvorg.model.SubdivisionDTO

/**
 * Mapper class for converting [SubdivisionDTO] objects to [SaveSubdivisionEntity] objects.
 * This mapper is responsible for transforming subdivision data transfer objects (DTOs)
 * into entities that can be stored in a database.
 */
class SaveSubdivisionDTOMapper : ISimpleMapper<SubdivisionDTO, SaveSubdivisionEntity> {

    /**
     * Maps a single [SubdivisionDTO] object to a [SaveSubdivisionEntity] object.
     *
     * @param input The input [SubdivisionDTO] object to be mapped.
     * @return A [SaveSubdivisionEntity] object containing the mapped data.
     */
    override fun map(input: SubdivisionDTO): SaveSubdivisionEntity = with(input) {
        // Create a new SaveSubdivisionEntity object using properties from SubdivisionDTO.
        SaveSubdivisionEntity(
            code = code,
            country = country,
            name = name
        )
    }

    /**
     * Maps a collection of [SubdivisionDTO] objects to a collection of [SaveSubdivisionEntity] objects.
     *
     * @param input The iterable collection of [SubdivisionDTO] objects to be mapped.
     * @return An iterable collection of [SaveSubdivisionEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<SubdivisionDTO>): Iterable<SaveSubdivisionEntity> =
        input.map(::map)
}