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

                    }

                    object Gemini {
                        private const val URL = "${Text.URL}/gemini"

                    }

                    object OpenAI {
                        private const val URL = "${Text.URL}/openai"

                    }
                }

                object Image {
                    private const val URL = "${Nutrients.URL}/image"

                    object OpenAI {
                        private const val URL = "${Image.URL}/openai"

                    }
                }
            }
        }

        object Login {
            private const val URL = "${Api.URL}/login"

            object Password {
                private const val URL = "${Login.URL}/password"

            }

            object Google {
                private const val URL = "${Login.URL}/google"

            }

            object Facebook {
                private const val URL = "${Login.URL}/facebook"

            }
        }

        object User {
            private const val URL = "${Api.URL}/user"

            suspend fun post(
                body: EmailPassword,
                headers: HeadersBuilder.() -> Unit = { {} }
            ): ResponseWrapper<Token> {
                return Endpoint(client = client, url = URL).post(body = body) {
                    headers()
                }
            }

            object Me {
                private const val URL = "${User.URL}/me"

                suspend fun get(headers: HeadersBuilder.() -> Unit): ResponseWrapper<io.wellmate.api.client.userData.Me> {
                    val endpoint = Endpoint(client = client, url = URL)
                    return endpoint.get { headers() }
                }
            }

            fun userId(userId: Int): Endpoint {
                return Endpoint(client, "$URL/$userId")
            }

            object Info {
                private const val URL = "${User.URL}/info"

                fun userId(userId: Int): Endpoint {
                    return Endpoint(client, "${URL}/$userId")
                }
            }

            object Activation {
                private const val URL = "${User.URL}/activation"

                fun resend(): Endpoint {
                    return Endpoint(client, "${URL}/resend")
                }
            }
        }

        object Entry {
            private const val URL = "${Api.URL}/entry"

            object Meal {
                private const val URL = "${Entry.URL}/meal"

                fun mealId(mealId: Int): Endpoint {
                    return Endpoint(client, "$URL/$mealId")
                }
            }

            object Timer {
                private const val URL = "${Entry.URL}/timer"

                fun timerId(timerId: Int): Endpoint {
                    return Endpoint(client, "$URL/$timerId")
                }
            }
        }
    }
}