package io.wellmate.api.client.dataclasses.entry

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface EntryBaseFieldsClient {
    val timestamp: Instant
    val note: String?
}


interface EntryBase : EntryBaseFieldsClient {
    val id: Int
    val userId: Int

    val added: Instant
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
    @Serializable(with = InstantIso8601Serializer::class) val added: Instant,
    @SerialName("user_id") val userId: Int,
    @Serializable(with = InstantIso8601Serializer::class) val timestamp: Instant,
    val type: EntryType,
)
