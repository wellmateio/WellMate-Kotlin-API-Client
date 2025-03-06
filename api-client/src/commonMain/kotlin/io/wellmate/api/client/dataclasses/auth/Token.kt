package io.wellmate.api.client.dataclasses.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String = "Bearer",
) {
    val authorizationHeader: String
        get() = "$tokenType $accessToken"
}
