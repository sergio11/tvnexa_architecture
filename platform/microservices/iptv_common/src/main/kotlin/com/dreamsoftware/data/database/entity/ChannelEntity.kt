package com.dreamsoftware.data.database.entity

data class ChannelEntity(
    val channelId: String,
    val name: String,
    val network: String? = null,
    val country: CountryEntity,
    val subdivision: SubdivisionEntity? = null,
    val city: String? = null,
    val isNsfw: Boolean,
    val website: String? = null,
    val logo: String,
    val launched: String? = null,
    val closed: String? = null,
    val replacedBy: ChannelEntity? = null,
    val languages: Iterable<LanguageEntity>,
    val categories: Iterable<CategoryEntity>
)
