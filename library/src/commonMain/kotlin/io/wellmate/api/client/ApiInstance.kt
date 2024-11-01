package io.wellmate.api.client

import io.wellmate.api.client.dataclasses.auth.Token
import kotlin.jvm.JvmStatic

class ApiInstance {
    var token: Token? = null

    init {
        instance = this
    }

    companion object {
        @JvmStatic
        lateinit var instance: ApiInstance
            private set
    }
}