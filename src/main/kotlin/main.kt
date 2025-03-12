package lol.malinovskaya

import lol.malinovskaya.repository.ProductsRepository
import lol.malinovskaya.repository.ProductsRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

fun main() {
    val di = DI {
        bindSingleton<ProductsRepository> { ProductsRepositoryImpl(di) }
        bindSingleton<ShopApplication> { ShopApplication(di) }
    }
    val application by di.instance<ShopApplication>()
    application.start()
}