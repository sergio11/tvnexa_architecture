package com.dreamsoftware.data.database.mapper

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.entity.CountryEntity
import com.dreamsoftware.data.database.entity.CountryTable
import org.jetbrains.exposed.sql.ResultRow

class CountryTableMapper: IOneSideMapper<ResultRow, CountryEntity> {

    override fun map(input: ResultRow): CountryEntity = with(input) {
        CountryEntity(
            name = get(CountryTable.name),
            code = get(CountryTable.code),
            flag = get(CountryTable.flag),
            languages = get(CountryTable.languages).split(",")
        )
    }

    override fun mapList(input: Iterable<ResultRow>): Iterable<CountryEntity> =
        input.map(::map)
}