package lol.malinovskaya.model.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Products : IntIdTable("products", "product_id") {
    val createDate = timestamp("create_date")
    val name = varchar("name", 511)
    val category = reference("category_id", ProductCategories)
    val price = integer("price")
    val currency = varchar("currency", 3)
    val description = text("description")
}

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)
    var name by Products.name
    var createDate by Products.createDate
    var category by ProductCategory referencedOn Products.category
    var price by Products.price
    var currency by Products.currency
    var description by Products.description

    val sizes by ProductSizeEntry referrersOn ProductSizeEntries.product
    val colors by ProductColor referrersOn ProductColors.product
    val images by ProductImage referrersOn ProductImages.product
}