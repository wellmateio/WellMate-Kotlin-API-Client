package io.wellmate.api.client.dataclasses.userData

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class PotentialUserFields(val email: String, val name: String, val info: String?)

@Serializable
data class PotentialUser(
    val email: String,
    val name: String,
    val info: String?,
    @Serializable(with = InstantIso8601Serializer::class) val added: Instant
)
