package com.example.login_with_firebase.Login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.login_with_firebase.R
import com.example.login_with_firebase.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.util.InputMismatchException

class SignInFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        if (authViewModel.checkCurrentUser()) {
            reload()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        binding.signInButton.setOnClickListener {
            try {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                authViewModel.signIn(email, password)
            } catch (e: InputMismatchException) {
                Snackbar.make(
                    binding.signInFragment,
                    "Please add Input to email and password", Snackbar.LENGTH_LONG
                ).show()
            }
        }
        binding.googleSignIn.setOnClickListener {
            try {
                authViewModel.googleSignIn(requireActivity(),getString(R.string.default_web_client_id))
            }catch (e:Exception){
                Snackbar.make(
                    binding.signInFragment,
                    "Something wrong please try again", Snackbar.LENGTH_LONG
                ).show()
            }

        }

        return binding.root
    }

    private fun reload() {
    }
}