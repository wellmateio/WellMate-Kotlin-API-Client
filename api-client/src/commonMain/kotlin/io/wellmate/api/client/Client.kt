@file:Suppress("PrivatePropertyName", "unused")

package io.wellmate.api.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.Parameters
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.wellmate.api.client.dataclasses.auth.EmailPassword
import io.wellmate.api.client.dataclasses.auth.OAuthToken
import io.wellmate.api.client.dataclasses.auth.Token
import io.wellmate.api.client.dataclasses.auth.UsernamePassword
import io.wellmate.api.client.dataclasses.chat.ChatMessage
import io.wellmate.api.client.dataclasses.chat.SendMessageRequest
import io.wellmate.api.client.dataclasses.entry.MealFields
import io.wellmate.api.client.dataclasses.entry.MealFieldsClient
import io.wellmate.api.client.dataclasses.entry.TimerFieldsClient
import io.wellmate.api.client.dataclasses.generics.Email
import io.wellmate.api.client.dataclasses.generics.Message
import io.wellmate.api.client.dataclasses.userData.PotentialUser
import io.wellmate.api.client.dataclasses.userData.PotentialUserFields
import io.wellmate.api.client.dataclasses.userData.UserInfo
import io.wellmate.api.client.dataclasses.userData.UserInfoFields
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.addLogging() {
    install(Logging) {
        level = LogLevel.ALL
    }
}

object WellMateClient {
    private const val URL = "https://wellmate-395510.lm.r.appspot.com"
    val client = HttpClient {
        addLogging()
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    object Api {
        private const val URL = "${WellMateClient.URL}/api"

        object AI {
            private const val URL = "${Api.URL}/ai"

            object Conversations {
                private const val URL = "${AI.URL}/conversations"

                object OpenAI {
                    private const val URL = "${Conversations.URL}/openai"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun post(
                        body: SendMessageRequest,
                        headers: HeadersBuilder.() -> Unit = { }
                    ): ResponseWrapper<ChatMessage> {
                        return endpoint.post(body = body) { headers() }
                    }
                }

                object History {
                    private const val URL = "${Conversations.URL}/history"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<List<ChatMessage>> {
                        return endpoint.get { headers() }
                    }
                }
            }

            object Nutrients {
                private const val URL = "${AI.URL}/nutrients"

                object Text {
                    private const val URL = "${Nutrients.URL}/text"

                    object ApiNinjas {
                        private const val URL = "${Text.URL}/api-ninjas"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: Message,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<MealFields> {
                            return endpoint.post(body = body) { headers() }
                        }
                    }

                    object Gemini {
                        private const val URL = "${Text.URL}/gemini"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: Message,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<MealFields> {
                            return endpoint.post(body = body) { headers() }
                        }
                    }

                    object OpenAI {
                        private const val URL = "${Text.URL}/openai"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: Message,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<MealFields> {
                            return endpoint.post(body = body) { headers() }
                        }
                    }
                }

                object Image {
                    private const val URL = "${Nutrients.URL}/image"

                    object OpenAI {
                        private const val URL = "${Image.URL}/openai"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: ByteReadChannel,
                            contentType: ContentType = ContentType.Image.JPEG,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<MealFields> {
                            return endpoint.post(
                                body = body,
                                contentType = contentType
                            ) { headers() }
                        }
                    }
                }
            }
        }

        object Login {
            private const val URL = "${Api.URL}/login"

            object Password {
                private const val URL = "${Login.URL}/password"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: UsernamePassword,
                    secChUaModel: String = "Unknown device",
                    secChUaPlatform: String = "N/A",
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Token> {

                    val body = FormDataContent(Parameters.build {
                        append("username", body.username)
                        append("password", body.password)
                    })
                    return endpoint.post(
                        body = body,
                        contentType = ContentType.Application.FormUrlEncoded,
                    ) {
                        headers {
                            headers()
                            append("sec-ch-ua-model", secChUaModel)
                            append("sec-ch-ua-platform", secChUaPlatform)
                        }
                    }
                }
            }

            object Google {
                private const val URL = "${Login.URL}/google"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: OAuthToken,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Token> {
                    return endpoint.post(body = body) { headers() }
                }
            }

            object Facebook {
                private const val URL = "${Login.URL}/facebook"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: OAuthToken,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Token> {
                    return endpoint.post(body = body) { headers() }
                }
            }
        }

        object User {
            private const val URL = "${Api.URL}/user"
            private val endpoint = Endpoint(client = client, url = URL)

            suspend fun post(
                body: EmailPassword,
                headers: HeadersBuilder.() -> Unit = { }
            ): ResponseWrapper<Token> {
                return endpoint.post(body = body) {
                    headers()
                }
            }

            object Me {
                private const val URL = "${User.URL}/me"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<io.wellmate.api.client.dataclasses.userData.Me> {
                    return endpoint.get { headers() }
                }
            }

            class UserId(private val userId: Int) {
                private val URL: String
                    get() = "${User.URL}/$userId"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun delete(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                    return endpoint.delete { headers() }
                }
            }

            object Info {
                private const val URL = "${User.URL}/info"
                private val endpoint = Endpoint(client = client, url = URL)

                class UserId(private val userId: Int) {
                    private val URL: String
                        get() = "${Info.URL}/$userId"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun post(
                        body: UserInfoFields,
                        headers: HeadersBuilder.() -> Unit = { }
                    ): ResponseWrapper<UserInfo> {
                        return endpoint.post(body = body) { headers() }
                    }

                    suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<UserInfo> {
                        return endpoint.get { headers() }
                    }
                }
            }

            object Activation {
                private const val URL = "${User.URL}/activation"

                object Resend {
                    private const val URL = "${Activation.URL}/resend"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun post(
                        body: Email,
                        headers: HeadersBuilder.() -> Unit = { }
                    ): ResponseWrapper<Any> {
                        return endpoint.post(body = body) { headers() }
                    }
                }

                class Activate(private val activationCode: String) {
                    private val URL: String
                        get() = "${Activation.URL}/activate/${activationCode}"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                        return endpoint.get { headers() }
                    }
                }
            }


            object Potential {
                private const val URL = "${User.URL}/potential"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: PotentialUserFields,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<PotentialUser> {
                    return endpoint.post(body = body) { headers() }
                }
            }
        }

        object Entry {
            private const val URL = "${Api.URL}/entry"
            private val endpoint = Endpoint(client = client, url = URL)

            suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<List<io.wellmate.api.client.dataclasses.entry.Entry>> {
                return endpoint.get { headers() }
            }

            object Meal {
                private const val URL = "${Entry.URL}/meal"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: MealFieldsClient,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<io.wellmate.api.client.dataclasses.entry.Meal> {
                    return endpoint.post(body = body) { headers() }
                }

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<List<io.wellmate.api.client.dataclasses.entry.Meal>> {
                    return endpoint.get { headers() }
                }

                class MealId(private val mealId: Int) {
                    private val URL: String
                        get() = "${Meal.URL}/$mealId"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun delete(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                        return endpoint.delete { headers() }
                    }
                }
            }

            object Timer {
                private const val URL = "${Entry.URL}/timer"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: TimerFieldsClient,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<io.wellmate.api.client.dataclasses.entry.Timer> {
                    return endpoint.post(body = body) { headers() }
                }

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<List<io.wellmate.api.client.dataclasses.entry.Timer>> {
                    return endpoint.get { headers() }
                }

                class TimerId(private val timerId: Int) {
                    private val URL: String
                        get() = "${Timer.URL}/$timerId"
                    private val endpoint = Endpoint(client = client, url = URL)

                    suspend fun delete(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                        return endpoint.delete { headers() }
                    }
                }
            }
        }

        object Admin {
            private const val URL = "${Api.URL}/admin"

            object PotentialUser {
                private const val URL = "${Admin.URL}/potential-user"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<List<io.wellmate.api.client.dataclasses.userData.PotentialUser>> {
                    return endpoint.get { headers() }
                }
            }
        }
    }
}