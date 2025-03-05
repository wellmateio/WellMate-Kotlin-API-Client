package io.wellmate.api.client.dataclasses.auth

import io.wellmate.api.client.Utils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsernamePassword(
    val username: String,
    val password: String,
    @SerialName("grant_type") val grantType: String = "password"
) {
    init {
        require(
            // We treat email as username
            Utils.Email.validate(email = username)
        )
        require(
            Utils.Password.validate(password = password)
        )
    }
}
