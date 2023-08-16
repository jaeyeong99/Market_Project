package com.example.marketproject.activity

import android.accounts.Account
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import com.example.marketproject.R
import com.example.marketproject.databinding.ActivityMainBinding
import com.example.marketproject.fragment.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mainActivity: MainActivity

    var auth : FirebaseAuth? = null
    var currentUser: FirebaseUser? = null

    val homeDetailFragment = HomeDetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        mainActivity = this

        auth = Firebase.auth
        currentUser = auth?.currentUser

        setContentView(binding.root)

        binding.bottomNavigationView.run {
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.item_home_fragment -> {
                        setFragment("HomeFragment")
                    }
                    R.id.item_community_fragment -> {
                        setFragment("CommunityFragment")
                    }
                    R.id.item_chatting_fragment -> {
                        setFragment("ChattingFragment")
                    }
                    R.id.item_account_fragment -> {
                        setFragment("AccountFragment")
                    }
                }
                true
            }
        }

        if (currentUser != null) {
            setFragment("HomeFragment")
            binding.bottomNavigationView.visibility = View.VISIBLE
            supportActionBar?.show()
        } else {
            setFragment("LoginFragment")
            binding.bottomNavigationView.visibility = View.GONE
            supportActionBar?.hide()
        }

        binding.floatingActionButton.setOnClickListener {
            if(supportFragmentManager.findFragmentByTag("HomeFragment") is HomeFragment) {
                setFragment("WriteFragment")
            }
        }



    }



    fun setFragment(tag: String){
        val transaction = supportFragmentManager.beginTransaction()

        when(tag){
            "LoginFragment"->{
                transaction.replace(R.id.fragment_container, LoginFragment(), "LoginFragment")
                mainActivity.setBottomNavigationVisibility(View.GONE)
                supportActionBar?.hide()
            }
            "SignUpFragment"->{
                transaction.replace(R.id.fragment_container, SignUpFragment(), "SignUpFragment")
                mainActivity.setBottomNavigationVisibility(View.GONE)
                supportActionBar?.hide()
            }
            "HomeFragment"->{
                transaction.replace(R.id.fragment_container, HomeFragment(), "HomeFragment")
                binding.floatingActionButton.show()
            }
            "HomeDetailFragment"->{
                transaction.replace(R.id.fragment_container, homeDetailFragment, "HomeDetailFragment")
                binding.floatingActionButton.hide()
                mainActivity.setBottomNavigationVisibility(View.GONE)
            }
            "CommunityFragment"->{
                transaction.replace(R.id.fragment_container, CommunityFragment(), "CommunityFragment")
                binding.floatingActionButton.show()
            }
            "ChattingFragment"->{
                transaction.replace(R.id.fragment_container, ChattingFragment(), "ChattingFragment")
                binding.floatingActionButton.hide()
            }
            "AccountFragment"->{
                transaction.replace(R.id.fragment_container, AccountFragment(), "AccountFragment")
                binding.floatingActionButton.hide()
            }
            "WriteFragment"->{
                transaction.replace(R.id.fragment_container, WriteFragment(), "WriteFragment")
                mainActivity.setBottomNavigationVisibility(View.GONE)
                binding.floatingActionButton.hide()
            }

        }
        transaction.commit()
    }

    fun openHomeDetailFragment(bundle: Bundle){

        homeDetailFragment.arguments = bundle
        Log.d(TAG, "openHomeDetailFragment $bundle")
        setFragment("HomeDetailFragment")

    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigationView.visibility = visibility
    }


    fun handleSuccessLogin() {
        val userUid = auth?.currentUser?.uid.orEmpty() // null일시 빈값으로 변경
        val currentDB = Firebase.database.reference.child("Users").child(userUid)
        val userInfoMap = mutableMapOf<String,Any>()
        userInfoMap["userId"] = userUid
        userInfoMap["nickName"] = "nickname"
        currentDB.updateChildren(userInfoMap)

    }




}
