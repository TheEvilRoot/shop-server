package lol.malinovskaya.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ColorDTO(
    val hex: String,
    val name: String
)