package io.wellmate.api.client.entry

import kotlinx.serialization.Serializable


@Serializable
data class IngredientFields(
    val name: String,

    val amount: Float,

    val kilocalories: Int,
    val proteins: Float,
    val carbohydrates: Float,
    val fats: Float,
    val sugars: Float,
)

@Serializable
data class Ingredient(
    val id: Int,

    val name: String,
    val amount: Float,

    val kilocalories: Int,
    val proteins: Float,
    val carbohydrates: Float,
    val fats: Float,
    val sugars: Float,
)