package io.wellmate.api.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.reflect.*


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
            println(this.body.toString())
        }
        println(response.request.url)
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
