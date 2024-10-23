package io.wellmate.api.client.userData

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class EmailPassword(val email: String, val password: String) {
    init {

        /**
         * Email address pattern, same as [android.util.Patterns.EMAIL_ADDRESS]
         */
        require(email.matches(Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )))

        var (upperCase, lowerCase, digits, special) = listOf(0, 0, 0, 0)
        for (char in password) {
            when {
                char.isUpperCase() -> upperCase++
                char.isLowerCase() -> lowerCase++
                char.isDigit() -> digits++
                else -> special++
            }
        }
        require(upperCase>0 && lowerCase>0 && digits>0 && special>0)
    }
}