package io.wellmate.api.client

import io.ktor.http.headers
import io.ktor.http.isSuccess
import kotlin.test.Test
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.assertFalse

class ApiTest {

    private val testDispatcher = StandardTestDispatcher()

    @AfterTest
    fun tearDown() {
        testDispatcher.cancel()
    }

    @Test
    fun `api-user-me get fails with no headers`() = runTest(testDispatcher) {
        val endpoint = Client.Api.User.Me
        assertFalse(endpoint.get { headers { } }.status.isSuccess(), "/api/user/me:get should not be successful when no header is provided")
    }

    @Test
    fun `api-user post fails with malformed body`() = runTest(testDispatcher) {
        val endpoint = Client.Api.User
        assertFalse(endpoint.post(body = "") { headers { } }.status.isSuccess(), "/api/user:post should not be successful when body is empty")
    }
}