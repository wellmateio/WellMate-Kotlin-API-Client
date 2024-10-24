package io.wellmate.api.client.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OAuthToken(@SerialName("access_token") val accessToken: String)