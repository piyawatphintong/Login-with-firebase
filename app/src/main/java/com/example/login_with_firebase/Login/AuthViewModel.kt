package com.example.login_with_firebase.Login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    fun signIn(email: String, password: String) {
        authRepository.signIn(email, password).observeForever { result ->
            if (result.isSuccess) {
                _user.value = result.getOrNull()
                Log.d("Success","Yayyyy")
            } else {
                // handle error
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }

    fun checkCurrentUser():Boolean{
        return authRepository.checkCurrentUser()
    }
}