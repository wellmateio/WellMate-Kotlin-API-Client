package io.wellmate.api.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.http.ParametersBuilder
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo


data class ResponseWrapper<T : Any>(
    val response: HttpResponse,
    private val typeInfo: TypeInfo
) {
    val status: HttpStatusCode = response.status

    suspend fun body(): T {
        if (status.isSuccess()) {
            return response.body(typeInfo) as T
        } else {
            throw IllegalStateException("Response was not successful: $status")
        }
    }
}

class Endpoint(val client: HttpClient, val url: String) {

    suspend inline fun <reified T : Any> get(
        crossinline headers: HeadersBuilder.() -> Unit = { },
    ): ResponseWrapper<T> {
        val response: HttpResponse = client.get(url) {
            headers {
                headers()
            }
        }
        return ResponseWrapper(response, typeInfo<T>())
    }

    suspend inline fun <reified T : Any> submitForm(
        crossinline formParameters: ParametersBuilder.() -> Unit = {},
        crossinline headers: HeadersBuilder.() -> Unit = { },
    ): ResponseWrapper<T> {
        val response: HttpResponse = client.submitForm(url = url,
            formParameters = parameters {
                formParameters()
            }) {
            headers {
                headers()
            }
        }
        return ResponseWrapper(response, typeInfo<T>())
    }

    suspend inline fun <reified T : Any> post(
        body: Any,
        contentType: ContentType = ContentType.Application.Json,
        crossinline headers: HeadersBuilder.() -> Unit = { { } }
    ): ResponseWrapper<T> {
        val response: HttpResponse = client.post(url) {
            headers {
                headers()
            }
            contentType(contentType)
            setBody(body)
        }
        return ResponseWrapper(response, typeInfo<T>())
    }

    suspend inline fun <reified T : Any> delete(
        crossinline headers: HeadersBuilder.() -> Unit = { { } }
    ): ResponseWrapper<T> {
        val response: HttpResponse = client.delete(url) {
            headers {
                headers()
            }
        }
        return ResponseWrapper(response, typeInfo<T>())
    }
}
