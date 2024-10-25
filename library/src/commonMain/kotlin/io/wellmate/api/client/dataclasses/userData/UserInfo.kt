package io.wellmate.api.client.dataclasses.userData

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Sex {
    @SerialName("m")
    M,

    @SerialName("f")
    F,
}

@Serializable
data class UserInfoFields(
    val name: String?,
    val surname: String?,

    @Serializable(with = InstantIso8601Serializer::class) val birthday: Instant?,
    val sex: Sex?,
    val weight: Int?,
    val height: Int?,
    val country: String?,
)

@Serializable
data class UserInfo(
    @SerialName("user_id") val userId: Int?,
    val name: String?,
    val surname: String?,

    @Serializable(with = InstantIso8601Serializer::class) val birthday: Instant?,
    val sex: Sex?,
    val weight: Int?,
    val height: Int?,
    val country: String?,
)