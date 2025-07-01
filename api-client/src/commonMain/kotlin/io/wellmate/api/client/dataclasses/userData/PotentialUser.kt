package io.wellmate.api.client.dataclasses.userData

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PotentialUserFields(val email: String, val name: String, val info: String?)

@Serializable
data class PotentialUser(
    val email: String,
    val name: String,
    val info: String?,
    @Serializable() val added: Instant
)
