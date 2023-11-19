package com.example.ecommerce_app.feature_auth.domain.use_case.login.login_google_login

import android.app.Application
import android.content.IntentSender
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.domain.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetGoogleSigInIntentSenderUseCase @Inject constructor(
    private val onTapClient: SignInClient,
    private val context: Application
){
    suspend operator fun invoke(): Resource<IntentSender?> {
        return try {
            val signInRequest = buildSignInRequest()
            val  beginSignInResult = onTapClient.beginSignIn(signInRequest).await()
            val intentSender = beginSignInResult.pendingIntent.intentSender
            Resource.Success(intentSender)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}