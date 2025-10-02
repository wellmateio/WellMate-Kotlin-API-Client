package io.wellmate.api.client.dataclasses.chat

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(val message: String)
