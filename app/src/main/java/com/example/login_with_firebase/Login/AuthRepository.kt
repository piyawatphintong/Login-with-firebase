package com.example.login_with_firebase.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String): LiveData<Result<User>> {
        val resultLiveData = MutableLiveData<Result<User>>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val user = User(
                        email = firebaseUser?.email,
                        displayName = firebaseUser?.displayName,
                    )
                    resultLiveData.value = Result.success(user)
                } else {
                    resultLiveData.value = Result.failure(task.exception!!)
                }
            }
        return resultLiveData
    }

    fun checkCurrentUser():Boolean{
        val currentUser = firebaseAuth.currentUser
        if (currentUser !=null){
            return true
        }
        return false
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}