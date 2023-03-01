package com.example.mvvm_order_management.model

import kotlinx.coroutines.delay
import kotlin.random.Random

object OrderDataGenerator {

    private val currentUsers = arrayListOf<User>()
    private val currentProducts = arrayListOf<Product>()
    private val currentOrders = arrayListOf<Order>()
    private const val stdDelay = 2000L

    init {
        for (i in 1..100) {
            generateUsers()
            generateProducts()
        }
        for (i in 1..100) generateOrders()
    }

    suspend fun getAllOrders(): List<Order> {
        delay(stdDelay)
        return currentOrders
    }

    suspend fun searchOrders(query: String): List<Order> {
        delay(stdDelay)
        return currentOrders.filter { order ->
            order.product.name.lowercase().contains(query.lowercase()) or
                    order.product.brand.lowercase().contains(query.lowercase()) or
                    order.user.name.lowercase().contains(query.lowercase()) or
                    order.user.email.lowercase().contains(query.lowercase())
        }
    }

    private fun generateUsers() {
        val index = Random.nextInt(usersList.size)
        val userName = usersList[index]
        val email = "$userName@gmail.com"
        val userId = 1000 + currentUsers.size
        currentUsers.add(User(userId, userName, email))
    }

    private fun generateProducts() {
        val brandIndex = Random.nextInt(brandsList.size)
        val brandName = brandsList[brandIndex]
        val productIndex = Random.nextInt(productsList.size)
        val productName = brandsList[productIndex]
        val price = Random.nextInt(1000)
        val productId = 2000 + currentProducts.size
        currentProducts.add(Product(productId, productName, brandName, price))
    }

    private fun generateOrders() {
        val user = currentUsers[Random.nextInt(currentUsers.size)]
        val product = currentProducts[Random.nextInt(currentProducts.size)]
        val orderId = 3000 + currentOrders.size
        currentOrders.add(Order(orderId, user, product))
    }
}