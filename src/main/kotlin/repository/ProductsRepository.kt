package lol.malinovskaya.repository

import lol.malinovskaya.model.api.ImageDTO
import lol.malinovskaya.model.api.ProductDTO

interface ProductsRepository {

    suspend fun getProductCatalog(ignoreCache: Boolean = false): List<ProductDTO>

    suspend fun getImageData(imageId: Int): ImageDTO

}