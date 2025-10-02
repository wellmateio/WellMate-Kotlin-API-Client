package io.wellmate.api.client.dataclasses.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ChatMessage(
    val id: Int,
    @Serializable() val timestamp: Instant,
    @SerialName("user_id") val userId: Int,
    @SerialName("user_message") val userMessage: Boolean,
    val message: String
)
