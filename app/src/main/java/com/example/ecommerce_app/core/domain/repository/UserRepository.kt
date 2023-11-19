package com.example.ecommerce_app.core.domain.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.SyncStateContract
import android.util.Log
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.util.Constant
import com.example.ecommerce_app.feature_shopping.data.model.Address
import com.example.ecommerce_app.feature_shopping.domain.repository.fieldName
import com.example.ecommerce_app.feature_shopping.domain.repository.value
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.net.URI
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: StorageReference,
    private val contentResolver: ContentResolver
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

    suspend fun updateUser(oldUser: User, newUser: User) {
        val updates = mutableMapOf<fieldName, value>()
        updates["firstName"] = newUser.firstName
        updates["lastName"] = newUser.lastName
        updates["email"] = newUser.email

        if (oldUser.imageUrl != newUser.imageUrl) {
            val imageUrl = uploadUserImageAndGetUrl(newUser.imageUrl)
            updates["imageUrl"] = imageUrl
        }

        getUserDocument(oldUser.email).set(updates)
    }

    private fun fromUserToMap(user: User) = mapOf(
        "firstName" to user.firstName,
        "lastName" to user.lastName,
        "email" to user.email,
        "imageUrl" to user.imageUrl
    )

    private suspend fun uploadUserImageAndGetUrl(imageUri: String): String {
        val uri = Uri.parse(imageUri)!!
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 96, stream)
        val imageByteArray = stream.toByteArray()


        val imageDirectory = storage.child("${Constant.USER_FOLDER}/${Constant.IMAGE_FILE}")
        val result = imageDirectory.putBytes(imageByteArray).await()
        return result.storage.downloadUrl.await().toString()
    }

}