@file:Suppress("PrivatePropertyName", "unused")

package io.wellmate.api.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HeadersBuilder
import io.ktor.serialization.kotlinx.json.json
import io.wellmate.api.client.auth.EmailPassword
import io.wellmate.api.client.auth.Token
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.addLogging() {
    install(Logging) {
        level = LogLevel.ALL
    }
}

object Client {
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
        private const val URL = "${Client.URL}/api"

        object AI {
            private const val URL = "${Api.URL}/ai"

            object Nutrients {
                private const val URL = "${AI.URL}/nutrients"

                object Text {
                    private const val URL = "${Nutrients.URL}/text"

                    object ApiNinjas {
                        private const val URL = "${Text.URL}/api-ninjas"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: Any,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<Any> {
                            return endpoint.post(body = body) { headers() }
                        }
                    }

                    object Gemini {
                        private const val URL = "${Text.URL}/gemini"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: Any,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<Any> {
                            return endpoint.post(body = body) { headers() }
                        }
                    }

                    object OpenAI {
                        private const val URL = "${Text.URL}/openai"
                        private val endpoint = Endpoint(client = client, url = URL)

                        suspend fun post(
                            body: Any,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<Any> {
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
                            body: Any,
                            headers: HeadersBuilder.() -> Unit = { }
                        ): ResponseWrapper<Any> {
                            return endpoint.post(body = body) { headers() }
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
                    body: Any,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Any> {
                    return endpoint.post(body = body) { headers() }
                }
            }

            object Google {
                private const val URL = "${Login.URL}/google"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: Any,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Any> {
                    return endpoint.post(body = body) { headers() }
                }
            }

            object Facebook {
                private const val URL = "${Login.URL}/facebook"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: Any,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Any> {
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

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<io.wellmate.api.client.userData.Me> {
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
                        body: Any,
                        headers: HeadersBuilder.() -> Unit = { }
                    ): ResponseWrapper<Any> {
                        return endpoint.post(body = body) { headers() }
                    }

                    suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
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
                        body: Any,
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

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                    return endpoint.get { headers() }
                }
            }
        }

        object Entry {
            private const val URL = "${Api.URL}/entry"
            private val endpoint = Endpoint(client = client, url = URL)

            suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                return endpoint.get { headers() }
            }

            object Meal {
                private const val URL = "${Entry.URL}/meal"
                private val endpoint = Endpoint(client = client, url = URL)

                suspend fun post(
                    body: Any,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Any> {
                    return endpoint.post(body = body) { headers() }
                }

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
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
                    body: Any,
                    headers: HeadersBuilder.() -> Unit = { }
                ): ResponseWrapper<Any> {
                    return endpoint.post(body = body) { headers() }
                }

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
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

                suspend fun get(headers: HeadersBuilder.() -> Unit = { }): ResponseWrapper<Any> {
                    return endpoint.get { headers() }
                }
            }
        }
    }
}