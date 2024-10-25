package io.wellmate.api.client

import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.wellmate.api.client.dataclasses.auth.EmailPassword
import io.wellmate.api.client.dataclasses.auth.Token
import io.wellmate.api.client.dataclasses.entry.MealFieldsClient
import io.wellmate.api.client.dataclasses.userData.Me
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.*
import kotlin.random.Random
import kotlin.test.*

class ApiTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var token: Token
    private lateinit var me: Me

    private val currentTime = Instant.fromEpochSeconds(1)

    @BeforeTest
    fun `set up the user for testing`() = runTest(testDispatcher) {
        val endpoint = Client.Api.User

        // Fake credentials generated for test purposes only
        val emailPassword = EmailPassword(
            email = "tester${Random.nextInt()}@wellmate.test",
            password = "nFbz\$Qc%!!@PgLl@5\\$^pH47XW9vl2D!SEp@b",
        )
        val resultCreate = endpoint.post(body = emailPassword)
        assertTrue(resultCreate.status.isSuccess())

        token = resultCreate.body()

        val resultMe = Client.Api.User.Me.get {
            append(
                HttpHeaders.Authorization,
                "${token.tokenType} ${token.accessToken}",
            )
        }
        assertTrue(resultMe.status.isSuccess())
        me = resultMe.body()
    }

    @AfterTest
    fun `delete the user used for testing`() = runTest(testDispatcher) {
        val deleteEndpoint = Client.Api.User.UserId(userId = me.id)
        val resultDelete = deleteEndpoint.delete {
            append(
                HttpHeaders.Authorization,
                "${token.tokenType} ${token.accessToken}",
            )
        }
        assertTrue(resultDelete.status.isSuccess())
    }

    @Test
    fun `api-user-me get fails with no headers`() = runTest(testDispatcher) {
        val endpoint = Client.Api.User.Me
        assertFalse(
            endpoint.get { }.status.isSuccess(),
            "/api/user/me:get should not be successful when no header is provided"
        )
    }

    @Test
    fun `api-entry post meal and delete afterwards`() = runTest(testDispatcher) {
        val mealEndpoint = Client.Api.Entry.Meal
        val mealFieldsClient = MealFieldsClient(
            name = "Chicken soup",
            timestamp = currentTime,
            ingredients = emptyList(),
        )
        val mealRequest = mealEndpoint.post(body = mealFieldsClient) {
            append(
                HttpHeaders.Authorization,
                token.authorizationHeader,
            )
        }
        assertTrue(mealRequest.status.isSuccess())
        assertIs<Instant>(mealRequest.body().added)

        val mealDeleteRequest = Client.Api.Entry.Meal.MealId(mealRequest.body().id).delete {
            append(
                HttpHeaders.Authorization,
                token.authorizationHeader,
            )
        }
        assertTrue(mealDeleteRequest.status.isSuccess())
    }
}
