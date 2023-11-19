package com.example.ecommerce_app.feature_auth.domain.use_case.login.login_google_login

import android.content.Intent
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.core.domain.use_case.user.AddUserUseCase
import com.example.ecommerce_app.core.domain.util.Resource
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleSignInUsingIntentUseCase @Inject constructor(
    private val onTapClient: SignInClient,
    private val auth: FirebaseAuth,
    private val addUserUseCase: AddUserUseCase
) {
    suspend operator fun invoke(intent: Intent): Resource<Unit> {
        val credential = onTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val firebaseUser = auth.signInWithCredential(googleCredentials).await().user
            val user = getUserFormFireBaseUser(firebaseUser)
            addUserUseCase(user)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    private fun getUserFormFireBaseUser(firebaseUser: FirebaseUser?) = User(
        firstName = firebaseUser.toString(),
        lastName = "",
        email = firebaseUser.toString(),
        imageUrl = firebaseUser.toString()
    )

}