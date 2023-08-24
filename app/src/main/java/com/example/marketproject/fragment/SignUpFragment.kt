package com.example.marketproject.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.databinding.FragmentHomeBinding
import com.example.marketproject.databinding.FragmentSignUpBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mainActivity: MainActivity



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)

        init()
        clickListener()

        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
    }

    private fun clickListener(){
        binding.btnSignUp.setOnClickListener {
            createAccount(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun createAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mainActivity.auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(mainActivity) { task ->
                if (task.isSuccessful) {
                    successSignUp()
                    mainActivity.setFragment(LoginFragment(), "LoginFragment")
                    Toast.makeText(mainActivity, "가입 완료", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(mainActivity, "가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun successSignUp() {
        val userUid = mainActivity.auth?.currentUser?.uid.orEmpty() // null일시 빈값으로 변경
        val currentDB = Firebase.database.reference.child("Users").child(userUid)
        val userInfoMap = mutableMapOf<String,Any>()
        userInfoMap["userId"] = userUid
        userInfoMap["nickName"] = binding.etNickName.text.toString()
        currentDB.updateChildren(userInfoMap)

    }
}