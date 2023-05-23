package com.example.login_with_firebase.Login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    fun signIn(email: String, password: String): LiveData<Result<User>> {
        val resultLiveData = MutableLiveData<Result<User>>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val user = User(
                        email = firebaseUser?.email,
                        displayName = firebaseUser?.displayName,
                        password = password,
                    )
                    resultLiveData.value = Result.success(user)
                } else {
                    resultLiveData.value = Result.failure(task.exception!!)
                }
            }
        return resultLiveData
    }

    fun googleSignIn(activity: Activity, IdToken: String) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(IdToken)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(data: Intent?, onFirebaseUserRetrieved: (FirebaseUser?) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        onFirebaseUserRetrieved(firebaseAuth.currentUser)
                    } else {
                        // Handle sign-in failure
                        onFirebaseUserRetrieved(null)
                    }
                }
        } catch (e: ApiException) {
            // Handle sign-in failure
            onFirebaseUserRetrieved(null)
        }
    }

    fun saveUserToFirebase(userdata: User) {
        db.collection("user").add(userdata).addOnSuccessListener { documentReference ->
            Log.d("documentReference", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w("documentReference", "Error adding document", e)
            }
    }

    fun checkCurrentUser(): Boolean {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            return true
        }
        return false
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}