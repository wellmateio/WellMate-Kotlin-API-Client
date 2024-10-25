package io.wellmate.api.client.dataclasses.auth

import io.wellmate.api.client.Utils
import kotlinx.serialization.*

@Serializable
data class EmailPassword(val email: String, val password: String) {
    init {
        require(
            Utils.Email.validate(email = email)
        )
        require(
            Utils.Password.validate(password = password)
        )
    }
}