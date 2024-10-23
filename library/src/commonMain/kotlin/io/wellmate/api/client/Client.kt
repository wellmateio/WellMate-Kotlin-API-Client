package io.wellmate.api.client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class Endpoint(private val client: HttpClient, private val url: String) {
    suspend fun get(headers: () -> Headers = { headers { } }): HttpResponse {
        return client.get(url) {
            headers()
        }
    }
    suspend fun post(body: Any, contentType: ContentType = ContentType.Application.Json, headers: () -> Headers = { headers { } }): HttpResponse {
        return client.post(url) {
            headers()
            contentType(contentType)
            setBody(body)
        }
    }
    suspend fun delete(headers: () -> Headers = { headers { } }): HttpResponse {
        return client.delete(url) {
            headers()
        }
    }
}


object Client {
    private const val URL = "https://wellmate-395510.lm.r.appspot.com"
    val client = HttpClient()

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

            suspend fun post(body: Any, headers: () -> Headers = { headers {} }): HttpResponse {
                return Endpoint(client=client, url = URL).post(body=body) {
                    headers()
                }
            }

            object Me {
                private const val URL = "${User.URL}/me"

                suspend fun get(headers: () -> Headers): HttpResponse {
                    return Endpoint(client=client, url=URL).get { headers() }
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