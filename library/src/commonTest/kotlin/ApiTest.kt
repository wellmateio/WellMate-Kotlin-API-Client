package io.wellmate.api.client

import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.wellmate.api.client.auth.EmailPassword
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.random.Random

class ApiTest {

    private val testDispatcher = StandardTestDispatcher()

    @AfterTest
    fun tearDown() {
        testDispatcher.cancel()
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
    fun `api-user post fails with malformed body due to validation`() = runTest(testDispatcher) {
        val endpoint = Client.Api.User
        assertFailsWith(
            IllegalArgumentException::class,
            "/api/user:post should not be successful when body is empty"
        ) {
            endpoint.post(body = EmailPassword("", ""))
        }
    }

    @Test
    fun `api-user post+delete succeeds with proper body + with me get`() = runTest(testDispatcher) {
        val endpoint = Client.Api.User

        // Fake credentials generated for test purposes only
        val emailPassword = EmailPassword(
            email = "tester${Random.nextInt()}@wellmate.test",
            password = "nFbz\$Qc%!!@PgLl@5\\$^pH47XW9vl2D!SEp@b",
        )

        val resultCreate = endpoint.post(body = emailPassword)
        assertTrue(resultCreate.status.isSuccess())
        val token = resultCreate.body()

        val resultMe = Client.Api.User.Me.get {
            append(
                HttpHeaders.Authorization,
                "${token.tokenType} ${token.accessToken}",
            )
        }
        assertTrue(resultMe.status.isSuccess())
        val me = resultMe.body()

        val deleteEndpoint = Client.Api.User.UserId(userId = me.id)
        val resultDelete = deleteEndpoint.delete {
            append(
                HttpHeaders.Authorization,
                "${token.tokenType} ${token.accessToken}",
            )
        }
        assertTrue(resultDelete.status.isSuccess())
    }
}