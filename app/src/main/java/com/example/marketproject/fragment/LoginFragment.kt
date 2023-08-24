package com.example.marketproject.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.databinding.ActivityMainBinding
import com.example.marketproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        init()
        clickListener()

        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
    }

    private fun clickListener(){
        binding.btnLogin.setOnClickListener {
            signIn(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
        binding.tvSignUp.setOnClickListener {
            mainActivity.setFragment(SignUpFragment(), "SignUpFragment")
        }
    }

    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(mainActivity) { task ->
                    if (task.isSuccessful) {
                        mainActivity.currentFragment = "LoginFragment"
                        mainActivity.binding.bottomNavigationView.selectedItemId = R.id.item_home_fragment
                        mainActivity.setFragment(HomeFragment(), "HomeFragment")
                        mainActivity.showNaviBarAndFloatingBtn()
                        mainActivity.handleSuccessLogin()
                        Toast.makeText(
                            mainActivity, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e(TAG, "Error signing in: ${task.exception}")
                        Toast.makeText(
                            mainActivity, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


}