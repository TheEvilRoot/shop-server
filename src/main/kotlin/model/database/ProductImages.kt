package lol.malinovskaya.model.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ProductImages : IntIdTable("product_images", "product_image_id") {
    val createDate = timestamp("create_date")
    val blob = blob("blob")
    val mediaType = varchar("media_type", 64)
    val product = reference("product_id", Products)
}

class ProductImage(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductImage>(ProductImages)
    var createDate by ProductImages.createDate
    var blob by ProductImages.blob
    var mediaType by ProductImages.mediaType
    var product by ProductImages.product
}