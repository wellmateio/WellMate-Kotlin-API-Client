package io.wellmate.api.client.dataclasses.entry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

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
    @Serializable() override val timestamp: Instant,
    override val note: String,

    override val type: TimerType,
    override val duration: Int,
) : EntryBaseFieldsClient, TimerInterface


@Serializable
data class Timer(
    override val id: Int,
    @SerialName("user_id") override val userId: Int,

    @Serializable() override val timestamp: Instant,
    @Serializable() override val added: Instant,
    override val note: String,

    override val type: TimerType,
    override val duration: Int,
) : EntryBase, TimerInterface
