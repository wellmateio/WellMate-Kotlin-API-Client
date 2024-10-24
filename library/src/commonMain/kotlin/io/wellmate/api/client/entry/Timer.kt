package io.wellmate.api.client.entry

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TimerInterface {
    val duration: Int
}

@Serializable
data class TimerFieldsClient(
    @Serializable(with = LocalDateTimeIso8601Serializer::class) override val timestamp: LocalDateTime,

    override val duration: Int,
) : EntryBaseFieldsClient, TimerInterface


@Serializable
data class Timer(
    override val id: Int,
    @SerialName("user_id") override val userId: Int,

    @Serializable(with = LocalDateTimeIso8601Serializer::class) override val timestamp: LocalDateTime,
    @Serializable(with = LocalDateTimeIso8601Serializer::class) override val added: LocalDateTime,

    override val duration: Int,
) : EntryBase, TimerInterface
