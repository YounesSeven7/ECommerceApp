package com.example.ecommerce_app.core.domain.repository

import com.example.ecommerce_app.core.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun register(user: User, password: String) {
        auth.createUserWithEmailAndPassword(user.email, password).await().user?.sendEmailVerification()
        logout()
    }

    suspend fun login(email: String, password: String): FirebaseUser? {
        return auth.signInWithEmailAndPassword(email, password).await().user
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    fun logout() {
        auth.signOut()
    }
}