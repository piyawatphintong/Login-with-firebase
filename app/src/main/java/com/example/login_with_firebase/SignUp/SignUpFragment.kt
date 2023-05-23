package com.example.login_with_firebase.SignUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.login_with_firebase.Login.AuthViewModel
import com.example.login_with_firebase.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    //TODO: permission to write data to firebase still denied
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        binding.submitButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.UserEditText.text.toString()
            signUpViewModel.setUserData(email, password,name)
        }
        observer()
        return binding.root
    }

    private fun observer(){
        signUpViewModel.userData.observe(viewLifecycleOwner) {
                signUpViewModel.saveUserToFirebase()
        }
    }

}