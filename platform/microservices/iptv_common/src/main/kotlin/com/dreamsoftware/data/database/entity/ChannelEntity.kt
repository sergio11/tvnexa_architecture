package com.dreamsoftware.data.database.entity


data class ChannelEntity(
    val channelId: String,
    val name: String? = null,
    val network: String? = null,
    val country: CountryEntity,
    val subdivision: SubdivisionEntity? = null,
    val city: String? = null,
    val isNsfw: Boolean? = null,
    val website: String? = null,
    val logo: String? = null,
    val launched: String? = null,
    val closed: String? = null,
    val replacedBy: ChannelEntity? = null,
    val languages: Iterable<LanguageEntity>,
    val categories: Iterable<CategoryEntity>
)

data class SaveChannelEntity(
    val channelId: String,
    val name: String,
    val network: String? = null,
    val country: String,
    val subdivision: String? = null,
    val city: String? = null,
    val isNsfw: Boolean,
    val website: String? = null,
    val logo: String,
    val launched: String? = null,
    val closed: String? = null,
    val replacedBy: String? = null,
    val altNames: List<String>,
    val languages: Iterable<String>,
    val categories: Iterable<String>,
    val owners: List<String>,
    val broadcastArea: List<String>
) {

    fun toLanguagesByChannel(): Iterable<Pair<String, String>> =
        languages.map { Pair(channelId, it) }

    fun toCategoriesByChannel(): Iterable<Pair<String, String>> =
        categories.map { Pair(channelId, it) }

    fun toAltNamesByChannel(): Iterable<Pair<String, String>> =
        altNames.map { Pair(channelId, it) }

    fun toOwnersByChannel(): Iterable<Pair<String, String>> =
        owners.map { Pair(channelId, it) }

    fun toBroadcastAreaByChannel(): Iterable<Pair<String, String>> =
        broadcastArea.map { Pair(channelId, it) }
}
