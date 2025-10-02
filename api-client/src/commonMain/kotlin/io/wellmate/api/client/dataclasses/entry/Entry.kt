package io.wellmate.api.client.dataclasses.entry

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface EntryBaseFieldsClient {
    val timestamp: Instant
    val note: String
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
    @Serializable() val added: Instant,
    @SerialName("user_id") val userId: Int,
    @Serializable() val timestamp: Instant,
    val type: EntryType,
)
