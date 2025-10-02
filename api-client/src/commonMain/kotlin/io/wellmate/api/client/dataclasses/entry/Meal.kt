package io.wellmate.api.client.dataclasses.entry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

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
    @Serializable() override val timestamp: Instant,
    override val note: String,

    override val name: String,
    override val ingredients: List<IngredientFields>,
) : EntryBaseFieldsClient, MealInterface

@Serializable
data class Meal(
    override val id: Int,
    @SerialName("user_id") override val userId: Int,

    @Serializable() override val added: Instant,
    @Serializable() override val timestamp: Instant,
    override val note: String,

    override val name: String,
    override val ingredients: List<Ingredient>,
) : EntryBase, MealInterface
