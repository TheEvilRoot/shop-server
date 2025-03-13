package lol.malinovskaya.utils

import kotlinx.serialization.json.Json
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams
import kotlin.time.Duration

class RedisCache<T>(
    private val index: Int,
    private val deserializer: (String) -> T,
    private val serializer: (T) -> String
) {

    private val pool: JedisPool = JedisPool(System.getenv("SHOP_REDIS_HOST"), System.getenv("SHOP_REDIS_PORT").toInt())

    private fun <R> redis(block: (Jedis) -> R): R {
        return pool.resource.use { redis -> block(redis) }
    }

    fun get(key: String): T? = redis { r ->
        r.select(index)
        val value = r.get(key)
        value?.let { deserializer(it) }
    }

    fun put(key: String, value: T, timeout: Duration? = null) {
        redis { r ->
            r.select(index)
            val params = SetParams()
            if (timeout != null) { params.ex(timeout.inWholeSeconds) }
            r.set(key, serializer(value), params)
        }
    }
}

inline fun <reified T> redisCache(index: Int): RedisCache<T> {
    return RedisCache(index, Json::decodeFromString, Json::encodeToString)
}