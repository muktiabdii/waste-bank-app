package com.example.wastebank.data.repository

import com.example.wastebank.data.mapper.ProductMapper
import com.example.wastebank.data.model.CartItemData
import com.example.wastebank.data.model.PaymentData
import com.example.wastebank.data.model.ProductData
import com.example.wastebank.data.source.firebase.FirebaseService
import com.example.wastebank.domain.model.CartItemDomain
import com.example.wastebank.domain.model.PaymentDomain
import com.example.wastebank.domain.model.ProductDomain
import com.example.wastebank.domain.repository.ProductRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductRepositoryImpl : ProductRepository {

    private val auth = FirebaseService.auth
    private val db = FirebaseService.db

    override suspend fun getProducts(): List<ProductDomain> {
        return try {
            val snapshot = db.getReference("product").get().await()
            snapshot.children.mapNotNull { dataSnapshot ->
                val productData = dataSnapshot.getValue(ProductData::class.java)
                productData?.let { ProductMapper.mapToDomain(it) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductByName(name: String): ProductDomain? {
        return try {
            val snapshot = db.getReference("product").get().await()
            val product = snapshot.children.mapNotNull { dataSnapshot ->
                val productData = dataSnapshot.getValue(ProductData::class.java)
                productData?.let { ProductMapper.mapToDomain(it) }
            }.firstOrNull { it.name == name } // Cari produk berdasarkan nama

            product
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addToCart(product: ProductDomain): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val userId = currentUser.uid
        val cartRef = db.getReference("users").child(userId).child("cart").child(product.name)

        return try {
            val snapshot = cartRef.get().await()
            val currentQuantity = snapshot.child("quantity").getValue(Int::class.java) ?: 0
            val newQuantity = currentQuantity + 1

            val cartItemData = CartItemData(
                name = product.name,
                category = product.category,
                price = product.price,
                quantity = newQuantity,
                image = product.image,
                total = product.price * newQuantity
            )

            cartRef.setValue(cartItemData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCartItems(): Flow<List<CartItemDomain>> = callbackFlow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            close() // Tutup Flow jika user belum login
            return@callbackFlow
        }

        val userId = currentUser.uid
        val cartRef = db.getReference("users").child(userId).child("cart")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = snapshot.children.mapNotNull { dataSnapshot ->
                    val cartItem = dataSnapshot.getValue(CartItemData::class.java)
                    cartItem?.let {
                        CartItemDomain(
                            name = it.name,
                            category = it.category,
                            price = it.price,
                            quantity = it.quantity,
                            image = it.image,
                            total = it.total
                        )
                    }
                }
                trySend(cartItems).isSuccess // Kirim data terbaru ke Flow
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) // Tutup Flow jika error
            }
        }

        cartRef.addValueEventListener(listener)

        awaitClose { cartRef.removeEventListener(listener) } // Hapus listener saat tidak digunakan
    }


    override suspend fun removeFromCart(productId: String): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val userId = currentUser.uid
        val cartRef = db.getReference("users").child(userId).child("cart").child(productId)

        return try {
            cartRef.removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun payment(payment: PaymentDomain): Result<Boolean> {
        return try {
            val currentUser = auth.currentUser ?: return Result.failure(Exception("User belum login"))
            val userName = db.getReference("users").child(currentUser.uid).child("name").get().await().getValue(String::class.java) ?: "Unknown"

            val paymentRef = db.getReference("payments").child(payment.paymentMethod).push() // Generate ID unik dari Firebase
            val paymentId = paymentRef.key ?: return Result.failure(Exception("Gagal generate paymentId"))

            val timestamp = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = dateFormat.format(Date(timestamp))
            val hour = timeFormat.format(Date(timestamp))

            // Buat objek PaymentDomain dengan ID & User Info
            val updatedPayment = payment.copy(
                paymentId = paymentId,
                userId = currentUser.uid,
                userName = userName,
                date = date,
                hour = hour,
                items = emptyList()
            )

            paymentRef.setValue(updatedPayment).await()

            val itemsRef = paymentRef.child("items")
            payment.items.forEachIndexed { index, item ->
                val customIndex = index + 1
                itemsRef.child(customIndex.toString()).setValue(item).await()
            }

            db.getReference("users").child(currentUser.uid).child("cart").removeValue().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
