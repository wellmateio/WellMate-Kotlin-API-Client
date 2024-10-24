package io.wellmate.api.client.entry

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface MealInterface {
    val name: String
    val ingredients: List<Any>
}

@Serializable
data class MealFields(
    override val name: String,
    override val ingredients: List<IngredientFields>,
) : MealInterface

@Serializable
data class MealFieldsClient(
    @Serializable(with = LocalDateTimeIso8601Serializer::class) override val timestamp: LocalDateTime,

    override val name: String,
    override val ingredients: List<IngredientFields>,
) : EntryBaseFieldsClient, MealInterface

@Serializable
data class Meal(
    override val id: Int,
    @SerialName("user_id") override val userId: Int,

    @Serializable(with = LocalDateTimeIso8601Serializer::class) override val added: LocalDateTime,
    @Serializable(with = LocalDateTimeIso8601Serializer::class) override val timestamp: LocalDateTime,

    override val name: String,
    override val ingredients: List<Ingredient>,
) : EntryBase, MealInterface