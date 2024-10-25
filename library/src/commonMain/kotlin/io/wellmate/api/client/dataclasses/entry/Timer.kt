package io.wellmate.api.client.dataclasses.entry

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TimerInterface {
    val duration: Int
}

@Serializable
data class TimerFieldsClient(
    @Serializable(with = InstantIso8601Serializer::class) override val timestamp: Instant,

    override val duration: Int,
) : EntryBaseFieldsClient, TimerInterface


@Serializable
data class Timer(
    override val id: Int,
    @SerialName("user_id") override val userId: Int,

    @Serializable(with = InstantIso8601Serializer::class) override val timestamp: Instant,
    @Serializable(with = InstantIso8601Serializer::class) override val added: Instant,

    override val duration: Int,
) : EntryBase, TimerInterface
