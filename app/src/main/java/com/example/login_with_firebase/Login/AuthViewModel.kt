package com.example.login_with_firebase.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun signIn(email: String, password: String) {
        authRepository.signIn(email, password).observeForever { result ->
            if (result.isSuccess) {
                _user.value = result.getOrNull()
            } else {
                // handle error
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }
}