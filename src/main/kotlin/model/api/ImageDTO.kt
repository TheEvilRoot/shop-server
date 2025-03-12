package lol.malinovskaya.model.api

import kotlinx.serialization.Serializable

@Serializable
data class ImageDTO(
    val bytes: ByteArray,
    val mediaType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageDTO

        if (!bytes.contentEquals(other.bytes)) return false
        if (mediaType != other.mediaType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + mediaType.hashCode()
        return result
    }
}