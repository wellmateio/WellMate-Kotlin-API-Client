package io.wellmate.api.client.userData

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer

@Serializable
enum class Sex {
    @SerialName("m") M,
    @SerialName("f") F,
}

@Serializable
data class UserInfoFields(
    val name: String?,
    val surname: String?,

    @Serializable(with = LocalDateTimeIso8601Serializer::class) val birthday: LocalDateTime?,
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

    @Serializable(with = LocalDateTimeIso8601Serializer::class) val birthday: LocalDateTime?,
    val sex: Sex?,
    val weight: Int?,
    val height: Int?,
    val country: String?,
)