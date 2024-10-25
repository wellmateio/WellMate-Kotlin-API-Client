package io.wellmate.api.client

import io.wellmate.api.client.dataclasses.auth.EmailPassword
import io.wellmate.api.client.dataclasses.auth.Token
import io.wellmate.api.client.dataclasses.entry.Ingredient
import io.wellmate.api.client.dataclasses.entry.Meal
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class EmailPasswordTest {
    @Test
    fun `EmailPassword fails on incorrect email`() {
        assertFailsWith(
            IllegalArgumentException::class,
            "EmailPassword validations should work"
        ) {
            EmailPassword("", "1!aA-123123123")
        }
    }

    @Test
    fun `EmailPassword fails on weak password`() {
        assertFailsWith(
            IllegalArgumentException::class,
            "EmailPassword validations should work"
        ) {
            EmailPassword("asd@test.test", "123123123")
        }
    }

    @Test
    fun `EmailPassword succeeds`() {
        assertIs<EmailPassword>(
            EmailPassword("asd@test.test", "1!aA-123123123"),
            "EmailPassword should be possible to set up"
        )
    }
}

class MealTest {

    private val currentTime: Instant = Clock.System.now()
    private val presetTime: Instant = Instant.fromEpochSeconds(1)

    @Test
    fun `Meal succeeds to create`() {
        assertIs<Meal>(
            Meal(
                id = 0,
                userId = 0,
                added = currentTime,
                timestamp = currentTime,
                name = "",
                ingredients = emptyList()
            )
        )
    }

    @Test
    fun `Meal is serializable properly`() {
        val meal = Meal(
            id = 0,
            userId = 0,
            added = presetTime,
            timestamp = presetTime,
            name = "",
            ingredients = emptyList()
        )
        val expectedJson =
            "{\"id\":0,\"user_id\":0,\"added\":\"1970-01-01T00:00:01Z\",\"timestamp\":\"1970-01-01T00:00:01Z\",\"name\":\"\",\"ingredients\":[]}"
        val json = Json.encodeToString(meal)
        assertEquals(
            expected = expectedJson,
            actual = json,
            message = "Meal should be serializable"
        )
    }

    @Test
    fun `Meal is deserializable properly`() {
        val json =
            "{\"id\":0,\"user_id\":0,\"added\":\"1970-01-01T00:00:01Z\",\"timestamp\":\"1970-01-01T00:00:01Z\",\"name\":\"\",\"ingredients\":[]}"
        val meal = Json.decodeFromString<Meal>(json)
        val expectedMeal = Meal(
            id = 0,
            userId = 0,
            added = presetTime,
            timestamp = presetTime,
            name = "",
            ingredients = emptyList()
        )
        assertEquals(
            expected = expectedMeal,
            actual = meal,
            message = "Meal should be deserializable"
        )
    }

    @Test
    fun `Meal is de+serializable properly with random data`() {
        val meal = Meal(
            id = Random.nextInt(),
            userId = Random.nextInt(),
            added = currentTime,
            timestamp = currentTime,
            name = "Test",
            ingredients = listOf(
                Ingredient(
                    id = Random.nextInt(),
                    name = "Test${Random.nextInt()}",
                    amount = Random.nextFloat(),
                    kilocalories = Random.nextInt(),
                    proteins = Random.nextFloat(),
                    carbohydrates = Random.nextFloat(),
                    fats = Random.nextFloat(),
                    sugars = Random.nextFloat(),
                ),
                Ingredient(
                    id = Random.nextInt(),
                    name = "Test${Random.nextInt()}",
                    amount = Random.nextFloat(),
                    kilocalories = Random.nextInt(),
                    proteins = Random.nextFloat(),
                    carbohydrates = Random.nextFloat(),
                    fats = Random.nextFloat(),
                    sugars = Random.nextFloat(),
                )
            )
        )
        val json = Json.encodeToString(meal)
        val actualMeal = Json.decodeFromString<Meal>(json)
        assertEquals(
            expected = meal,
            actual = actualMeal,
            message = "Meal should be (de)serializable"
        )
        assertEquals(
            expected = 2,
            actual = actualMeal.ingredients.size,
            message = "Meal should contain both ingredients"
        )
        assertEquals(
            expected = meal.ingredients[1].carbohydrates,
            actual = actualMeal.ingredients[1].carbohydrates,
            message = "Meal should contain ingredients with proper data"
        )
    }
}

class TokenTest {
    @Test
    fun `token authorization header is correct`() {
        val token = Token(
            tokenType = "Bearer",
            accessToken = "test",
        )
        assertEquals(
            expected = "Bearer test",
            actual = token.authorizationHeader,
            message = "Token should be able to generate authorization header"
        )
    }

    @Test
    fun `token is serializable properly`() {
        val token = Token(
            tokenType = "Bearer",
            accessToken = "test",
        )
        val expectedJson = "{\"access_token\":\"test\",\"token_type\":\"Bearer\"}"
        val json = Json.encodeToString(token)
        assertEquals(
            expected = expectedJson,
            actual = json,
            message = "Token should be serializable"
        )
    }
}
