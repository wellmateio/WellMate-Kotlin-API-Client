package io.wellmate.api.client.dataclasses.entry

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TimerInterface {
    val type: TimerType
    val duration: Int
}


@Serializable
enum class TimerType {
    @SerialName("meditation")
    MEDITATION,

    @SerialName("walk")
    WALK,

    @SerialName("pet")
    PET,

    @SerialName("nap")
    NAP,

    @SerialName("scrolling")
    SCROLLING,

    @SerialName("focus")
    FOCUS,

    @SerialName("sports")
    SPORTS,

    @SerialName("exercise")
    EXERCISE,
}

@Serializable
data class TimerFieldsClient(
    @Serializable(with = InstantIso8601Serializer::class) override val timestamp: Instant,
    override val note: String?,

    override val type: TimerType,
    override val duration: Int,
) : EntryBaseFieldsClient, TimerInterface


@Serializable
data class Timer(
    override val id: Int,
    @SerialName("user_id") override val userId: Int,

    @Serializable(with = InstantIso8601Serializer::class) override val timestamp: Instant,
    @Serializable(with = InstantIso8601Serializer::class) override val added: Instant,
    override val note: String?,

    override val type: TimerType,
    override val duration: Int,
) : EntryBase, TimerInterface
