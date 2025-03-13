package lol.malinovskaya

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.inmo.krontab.KronScheduler
import dev.inmo.krontab.everyMinute
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import lol.malinovskaya.repository.ProductsRepository
import lol.malinovskaya.utils.response
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import io.github.flaxoos.ktor.server.plugins.taskscheduling.*
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.redis.redis
import org.slf4j.LoggerFactory
import kotlin.time.measureTime

class ShopApplication(di: DI) : DIAware by di {

    private val productsRepository: ProductsRepository by instance()

    fun start() {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = { module() })
            .start(wait = true)
    }

    private fun configureDatabase() {
        val config = DatabaseConfig {
        }
        val poolConfig = HikariConfig().apply {
            jdbcUrl = System.getenv("SHOP_DATABASE_URL")
            username = System.getenv("SHOP_DATABASE_USER")
            password = System.getenv("SHOP_DATABASE_PASSWORD")
            maximumPoolSize = 8
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
        }
        val dataSource = HikariDataSource(poolConfig)
        Database.connect(dataSource, databaseConfig = config)

    }

    private fun Application.module() {
        configureDatabase()
        install(CORS) {
            allowCredentials = true
            allowNonSimpleContentTypes = true
            anyHost()
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
        }
        install(ContentNegotiation) {
            json()
        }
        install(CallId)
        install(CallLogging)
        install(StatusPages) {
            exception { call, e: Throwable ->
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, e.response())
            }
        }
        install(TaskScheduling) {
            redis {
                host = System.getenv("SHOP_REDIS_HOST")
                port = System.getenv("SHOP_REDIS_PORT").toInt()
            }
            task {
                val logger = LoggerFactory.getLogger("RefreshCatalogCache")
                name = "RefreshCatalogCache"
                concurrency = 1
                task = {
                    logger.warn("Refreshing catalog cache...")
                    val elapsed = measureTime { productsRepository.getProductCatalog(ignoreCache = true) }
                    logger.warn("Catalog cache refresh took $elapsed")
                }
                kronSchedule = {
                    seconds { 0 every 30 }
                }
            }
        }
        routing {
            route("api") {
                get("catalog") {
                    val products = productsRepository.getProductCatalog()
                    call.respond(products)
                }
                get("images/{image_id}") {
                    val imageId = call.parameters.getOrFail("image_id").toInt()
                    val image = productsRepository.getImageData(imageId)
                    call.response.cacheControl(CacheControl.MaxAge(maxAgeSeconds = 3600))
                    call.respondBytes(
                        contentType = ContentType.parse(image.mediaType),
                        HttpStatusCode.OK
                    ) { image.bytes }
                }
            }
        }
    }
}