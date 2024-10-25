package io.wellmate.api.client.dataclasses.generics

import io.wellmate.api.client.Utils
import kotlinx.serialization.Serializable

@Serializable
data class Email(val email: String) {
    init {
        require(
            Utils.Email.validate(email = email)
        )
    }
}
