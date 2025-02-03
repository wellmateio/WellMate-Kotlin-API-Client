package io.wellmate.api.client

import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.wellmate.api.client.dataclasses.auth.EmailPassword
import io.wellmate.api.client.dataclasses.entry.Meal
import io.wellmate.api.client.dataclasses.entry.MealFieldsClient
import io.wellmate.api.client.dataclasses.entry.Timer
import io.wellmate.api.client.dataclasses.entry.TimerFieldsClient
import io.wellmate.api.client.dataclasses.entry.TimerType
import io.wellmate.api.client.dataclasses.userData.Me
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ApiTest {

    private val testDispatcher = StandardTestDispatcher()

    private val apiInstance = ApiInstance()

    private lateinit var me: Me

    @BeforeTest
    fun `set up the user for testing`() = runTest(testDispatcher) {
        // Fake credentials generated for test purposes only
        val emailPassword = EmailPassword(
            email = "tester${Random.nextInt()}@wellmate.test",
            password = "nFbz\$Qc%!!@PgLl@5\\$^pH47XW9vl2D!SEp@b",
        )
        val resultCreate = WellMateClient.Api.User.post(body = emailPassword)
        assertTrue(resultCreate.status.isSuccess())

        apiInstance.token = resultCreate.body()

        val resultMe = WellMateClient.Api.User.Me.get()
        assertTrue(resultMe.status.isSuccess())
        me = resultMe.body()
    }

    @AfterTest
    fun `delete the user used for testing`() = runTest(testDispatcher) {
        val deleteEndpoint = WellMateClient.Api.User.UserId(userId = me.id)
        val resultDelete = deleteEndpoint.delete()
        assertTrue(resultDelete.status.isSuccess())
    }

    @Test
    fun `api-user-me get fails with no headers`() = runTest(testDispatcher) {
        val endpoint = WellMateClient.Api.User.Me
        assertFalse(
            endpoint.get { append(HttpHeaders.Authorization, "") }.status.isSuccess(),
            "/api/user/me:get should not be successful when no header is provided"
        )
    }

    private object Entries {
        private val currentTime = Instant.fromEpochSeconds(1)

        val MEAL = MealFieldsClient(
            name = "Chicken soup",
            timestamp = currentTime,
            note = null,
            ingredients = emptyList(),
        )
        val TIMER = TimerFieldsClient(
            timestamp = currentTime,
            type = TimerType.MEDITATION,
            duration = Random.nextInt(from = 100, until = 1000),
            note = "Evening meditation",
        )

        class Operations {
            suspend fun postMeal(): Meal {
                val mealEndpoint = WellMateClient.Api.Entry.Meal
                val mealResponse = mealEndpoint.post(body = MEAL)
                assertTrue(mealResponse.status.isSuccess())
                assertIs<Instant>(mealResponse.body().added)
                return mealResponse.body()
            }

            suspend fun deleteMeal(mealId: Int) {
                val mealEndpoint = WellMateClient.Api.Entry.Meal.MealId(mealId)
                val mealResponse = mealEndpoint.delete()
                assertTrue(mealResponse.status.isSuccess())
            }

            suspend fun postTimer(): Timer {
                val timerEndpoint = WellMateClient.Api.Entry.Timer
                val timerResponse = timerEndpoint.post(body = TIMER)
                assertTrue(timerResponse.status.isSuccess())
                assertIs<Instant>(timerResponse.body().added)
                assertEquals(expected = TIMER.note, actual = timerResponse.body().note)
                return timerResponse.body()
            }

            suspend fun deleteTimer(timerId: Int) {
                val timerEndpoint = WellMateClient.Api.Entry.Timer.TimerId(timerId)
                val timerResponse = timerEndpoint.delete()
                assertTrue(timerResponse.status.isSuccess())
            }
        }
    }

    @Test
    fun `api-entry post meal and delete afterwards`() = runTest(testDispatcher) {
        val operations = Entries.Operations()
        val meal = operations.postMeal()
        operations.deleteMeal(meal.id)
    }

    @Test
    fun `api-entry post timer and delete afterwards`() = runTest(testDispatcher) {
        val operations = Entries.Operations()
        val timer = operations.postTimer()
        operations.deleteTimer(timer.id)
    }


    @Test
    fun `api-entry post entry + timer and return both`() = runTest(testDispatcher) {
        val operations = Entries.Operations()

        val meal = operations.postMeal()
        val timer = operations.postTimer()

        val entriesEndpoint = WellMateClient.Api.Entry.get()

        assertTrue(entriesEndpoint.status.isSuccess())
        assertEquals(2, entriesEndpoint.body().size)

        operations.deleteMeal(meal.id)
        operations.deleteTimer(timer.id)
    }
}
