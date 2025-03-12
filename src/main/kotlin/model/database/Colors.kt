package lol.malinovskaya.model.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Colors : IntIdTable("colors", "color_id") {
    val value = varchar("value", 8).uniqueIndex("colors_pk")
    val name = varchar("name", 64)
}

class Color(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Color>(Colors)
    var value by Colors.value
    var name by Colors.name
}