package com.example.login_with_firebase.Login

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login_with_firebase.Provider.ResourceProvider
import com.example.login_with_firebase.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

//TODO: probleam here cause by resourceProvider still can't fix it out
class AuthViewModel(private val resourceProvider: ResourceProvider) : ViewModel() {
    private val authRepository = AuthRepository()

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    private val _signInResult = MutableLiveData<SignInResult>()
    val signInResult: LiveData<SignInResult> = _signInResult

    fun signIn(email: String, password: String) {
        authRepository.signIn(email, password).observeForever { result ->
            if (result.isSuccess) {
                _user.value = result.getOrNull()
                Log.d("Success", "Yayyyy")
            } else {
                // handle error
            }
        }
    }

    fun googleSignIn(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resourceProvider.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent

        _signInResult.value = SignInResult.Start(signInIntent)
    }

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            // Signed in successfully, handle the account
            _signInResult.value = SignInResult.Success(account)
        } catch (e: ApiException) {
            // Sign-in failed, handle the exception
            _signInResult.value = SignInResult.Failure(e)
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }

    fun checkCurrentUser(): Boolean {
        return authRepository.checkCurrentUser()
    }
}

sealed class SignInResult {
    data class Start(val signInIntent: Intent) : SignInResult()
    data class Success(val account: GoogleSignInAccount) : SignInResult()
    data class Failure(val exception: ApiException) : SignInResult()
}