package com.example.login_with_firebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.login_with_firebase.Login.AuthViewModel
import com.example.login_with_firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        authViewModel.user.observe(this) { user ->
            if (user != null) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.userProfileFragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.findDestination(R.id.userProfileFragment)
            }
        }
    }
}