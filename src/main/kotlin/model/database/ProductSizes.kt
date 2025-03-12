package lol.malinovskaya.model.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductSizeEntries : IntIdTable("product_sizes", "product_size_id") {
    val product = reference("product_id", Products)
    val size = enumerationByName<ProductSize>("size", 32)
}

class ProductSizeEntry(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductSizeEntry>(ProductSizeEntries)
    var size by ProductSizeEntries.size
    var product by ProductSizeEntries.product
}