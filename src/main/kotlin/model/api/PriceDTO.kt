package lol.malinovskaya.model.api

import kotlinx.serialization.Serializable

@Serializable
data class PriceDTO(
    val value: Int,
    val currency: String,
)