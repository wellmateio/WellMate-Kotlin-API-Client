package io.wellmate.api.client.entry

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface EntryBaseFieldsClient {
    val timestamp: LocalDateTime
}


interface EntryBase : EntryBaseFieldsClient {
    val id: Int
    val userId: Int

    val added: LocalDateTime
}

@Serializable
enum class EntryType {
    @SerialName("meal")
    MEAL,

    @SerialName("timer")
    TIMER,
}

@Serializable
data class Entry(
    val id: Int,
    @Serializable(with = LocalDateTimeIso8601Serializer::class) val added: LocalDateTime,
    @SerialName("user_id") val userId: Int,
    @Serializable(with = LocalDateTimeIso8601Serializer::class) val timestamp: LocalDateTime,
    val type: EntryType,
)