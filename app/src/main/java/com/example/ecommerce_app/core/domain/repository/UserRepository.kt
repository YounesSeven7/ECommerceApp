package com.example.ecommerce_app.core.domain.repository

import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun getUserCollection() = db.collection(Constant.USER_COLLECTION)

    private fun getUserDocument(email: String) = getUserCollection().document(email)

    fun getUserDocument() = getUserCollection().document(auth.currentUser!!.email!!)

    fun addUser(user: User) = getUserDocument(user.email).set(user)

    suspend fun getUser() = getUserDocument(auth.currentUser!!.email!!)
        .get().await().toObject(User::class.java)!!

    suspend fun getUser(email: String) = getUserDocument(email)
        .get().await().toObject(User::class.java)

    suspend fun isUserExist(email: String) = getUserDocument(email)
        .get().await().exists()




}