package lol.malinovskaya.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val type: String,
    val images: List<String>,
    val price: PriceDTO,
    val availableSizes: List<String>,
    val description: String,
    val color: ColorDTO
)