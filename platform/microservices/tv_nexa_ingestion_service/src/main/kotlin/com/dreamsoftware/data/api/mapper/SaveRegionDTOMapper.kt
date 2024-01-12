package com.dreamsoftware.data.api.mapper

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.entity.SaveRegionEntity
import com.dreamsoftware.data.iptvorg.model.RegionDTO

/**
 * Mapper class for converting [RegionDTO] objects to [SaveRegionEntity] objects.
 * This mapper is responsible for transforming region data transfer objects (DTOs)
 * into entities that can be stored in a database.
 */
class SaveRegionDTOMapper : ISimpleMapper<RegionDTO, SaveRegionEntity> {

    /**
     * Maps a single [RegionDTO] object to a [SaveRegionEntity] object.
     *
     * @param input The input [RegionDTO] object to be mapped.
     * @return A [SaveRegionEntity] object containing the mapped data.
     */
    override fun map(input: RegionDTO): SaveRegionEntity = with(input) {
        // Create a new SaveRegionEntity object using properties from RegionDTO.
        SaveRegionEntity(
            code = code,
            name = name,
            countries = countries
        )
    }

    /**
     * Maps a collection of [RegionDTO] objects to a collection of [SaveRegionEntity] objects.
     *
     * @param input The iterable collection of [RegionDTO] objects to be mapped.
     * @return An iterable collection of [SaveRegionEntity] objects containing the mapped data.
     */
    override fun mapList(input: Iterable<RegionDTO>): Iterable<SaveRegionEntity> =
        input.map(::map)
}