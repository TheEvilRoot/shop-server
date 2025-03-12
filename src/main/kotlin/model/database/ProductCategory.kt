package lol.malinovskaya.model.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductCategories : IntIdTable("product_categories", "category_id") {
    val name = varchar("name", 511).uniqueIndex("product_categories_pk_2")
}

class ProductCategory(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductCategory>(ProductCategories)
    var name by ProductCategories.name
}