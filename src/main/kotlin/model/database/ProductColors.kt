package lol.malinovskaya.model.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductColors : IntIdTable("product_colors", "product_color_id") {
    val product = reference("product_id", Products)
    val color = reference("color_id", Colors)
}

class ProductColor(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductColor>(ProductColors)
    var product by ProductColors.product
    var color by Color referencedOn ProductColors.color
}