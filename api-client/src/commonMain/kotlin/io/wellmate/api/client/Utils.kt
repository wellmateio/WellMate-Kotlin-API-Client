package io.wellmate.api.client


object Utils {
    object Email {

        /**
         * Email address pattern, same as [android.util.Patterns.EMAIL_ADDRESS]
         */
        private val REGEX = Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun validate(email: String): Boolean {
            return email.matches(REGEX)
        }
    }

    object Password {
        fun validate(password: String): Boolean {
            var (upperCase, lowerCase, digits, special) = listOf(0, 0, 0, 0)
            for (char in password) {
                when {
                    char.isUpperCase() -> upperCase++
                    char.isLowerCase() -> lowerCase++
                    char.isDigit() -> digits++
                    else -> special++
                }
            }
            return upperCase > 0 && lowerCase > 0 && digits > 0 && special > 0
        }
    }
}