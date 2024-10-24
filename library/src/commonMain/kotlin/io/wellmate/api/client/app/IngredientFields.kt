package io.wellmate.api.client.app

import kotlinx.serialization.Serializable


@Serializable
class IngredientFields(
    val name: String,
    val amount: Float,

    val kilocalories: Int,
    val proteins: Float,
    val carbohydrates: Float,
    val fats: Float,
    val sugars: Float,
)