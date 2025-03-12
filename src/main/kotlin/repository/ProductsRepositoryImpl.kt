package lol.malinovskaya.repository

import lol.malinovskaya.model.api.ColorDTO
import lol.malinovskaya.model.api.ImageDTO
import lol.malinovskaya.model.api.PriceDTO
import lol.malinovskaya.model.api.ProductDTO
import lol.malinovskaya.model.database.*
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.kodein.di.DI
import org.kodein.di.DIAware

class ProductsRepositoryImpl(di: DI) : ProductsRepository, DIAware by di {

    private val cdnUrl: String = System.getenv("SHOP_CDN_URL")

    private fun productImageToLink(image: ProductImage): String {
        return "${cdnUrl}/api/images/${image.id.value}"
    }

    private fun productColorToDto(color: Color?): ColorDTO {
        if (color == null) { return ColorDTO("000000", "black") }
        return ColorDTO(color.value, color.name)
    }

    private fun productToDto(product: Product): ProductDTO {
        return ProductDTO(
            type = product.category.name,
            images = product.images.map { productImageToLink(it) },
            price = PriceDTO(product.price, product.currency),
            availableSizes = product.sizes.map { it.size.name },
            description = product.description,
            color = productColorToDto(product.colors.firstOrNull()?.color)
        )
    }

    override suspend fun getProductCatalog(): List<ProductDTO> = newSuspendedTransaction {
        addLogger(StdOutSqlLogger)
        Product.all()
            .notForUpdate()
            .with(Product::category, Product::colors, Product::sizes, Product::images)
            .orderBy(Products.id to SortOrder.DESC)
            .limit(100)
            .map { productToDto(it) }
            .toList()
    }

    override suspend fun getImageData(imageId: Int): ImageDTO = newSuspendedTransaction {
        val image = ProductImage.findById(imageId) ?: throw IllegalStateException("Product image $imageId not found")
        ImageDTO(image.blob.bytes, image.mediaType)
    }
}