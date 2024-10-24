package io.wellmate.api.client.app

import kotlinx.serialization.Serializable

@Serializable
class MealFields(
    val name: String,
    val ingredients: List<IngredientFields>,
)