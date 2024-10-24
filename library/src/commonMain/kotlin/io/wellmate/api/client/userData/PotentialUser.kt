package io.wellmate.api.client.userData

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class PotentialUserFields(val email: String, val name: String, val info: String?)

@Serializable
data class PotentialUser(
    val email: String,
    val name: String,
    val info: String?,
    @Serializable(with = LocalDateTimeIso8601Serializer::class) val added: LocalDateTime
)
