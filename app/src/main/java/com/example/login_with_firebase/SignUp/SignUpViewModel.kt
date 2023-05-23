package com.example.login_with_firebase.SignUp

import android.service.autofill.UserData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login_with_firebase.Login.AuthRepository
import com.example.login_with_firebase.Login.User

class SignUpViewModel: ViewModel() {
    private val authRepository = AuthRepository()

    private val _userData = MutableLiveData<User?>()
    val userData: MutableLiveData<User?> = _userData

    fun saveUserToFirebase(){
        userData.value?.let { authRepository.saveUserToFirebase(it) }
    }

    fun setUserData(email:String?,password:String,name:String?){
        _userData.value = User(email,password,name)
    }
}