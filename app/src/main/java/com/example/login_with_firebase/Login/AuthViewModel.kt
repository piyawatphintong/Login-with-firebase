package com.example.login_with_firebase.Login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    private val _user2 = MutableLiveData<FirebaseUser?>()
    val user2: MutableLiveData<FirebaseUser?> = _user2

    private lateinit var signInRequest: BeginSignInRequest
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

    fun googleSignIn(activity: Activity, IdToken: String) {
        authRepository.googleSignIn(activity, IdToken)
    }

    fun handleSignInResult(data: Intent?) {
        authRepository.handleSignInResult(data) { firebaseUser ->
            _user2.value = firebaseUser
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
