package io.wellmate.api.client.dataclasses.userData

import kotlinx.serialization.Serializable

@Serializable
data class Me(val id: Int, val email: String, val type: Int)