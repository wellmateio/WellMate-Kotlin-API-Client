package io.wellmate.api.client.dataclasses.userData

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class PotentialUserFields(val email: String, val name: String, val info: String?)

@Serializable
data class PotentialUser(
    val email: String,
    val name: String,
    val info: String?,
    @Serializable() val added: Instant
)
