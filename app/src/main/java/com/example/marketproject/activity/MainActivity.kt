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

    private val globalHomeDetailFragment = HomeDetailFragment()

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
            showNaviBarAndFloatingBtn()
        } else {
            setFragment("LoginFragment")
            hideNaviBarAndFloatingBtn()
        }

        binding.floatingActionButton.setOnClickListener {
            if(supportFragmentManager.findFragmentByTag("HomeFragment") is HomeFragment) {
                setFragment("WriteFragment")
            }
        }



    }



    fun setFragment(tag: String){

        val transaction = supportFragmentManager.beginTransaction()
//        val fragmentManager = supportFragmentManager
//        val currentFragment = fragmentManager.primaryNavigationFragment
//        Log.d(TAG, "currentFragment: $currentFragment")
//        if (currentFragment != null) {
//            Log.d(TAG, "Current Fragment: ???")
//            transaction.hide(currentFragment)
//        }



        when(tag){
            "LoginFragment"->{
                transaction.add(R.id.fragment_container, LoginFragment(), "LoginFragment")
                mainActivity.setBottomNavigationVisibility(View.GONE)
                //supportActionBar?.hide()
            }
            "SignUpFragment"->{
                transaction.add(R.id.fragment_container, LoginFragment(), "SignUpFragment")
                transaction.addToBackStack(null)
                mainActivity.setBottomNavigationVisibility(View.GONE)
                //supportActionBar?.hide()
            }
            "HomeFragment"->{
                transaction.add(R.id.fragment_container, HomeFragment(), "HomeFragment")
                transaction.addToBackStack(null)
                showNaviBarAndFloatingBtn()
                //binding.floatingActionButton.show()
            }
            "HomeDetailFragment"->{
                transaction.add(R.id.fragment_container, globalHomeDetailFragment, "HomeDetailFragment")
                transaction.addToBackStack(null)
                hideNaviBarAndFloatingBtn()
//                binding.floatingActionButton.hide()
//                mainActivity.setBottomNavigationVisibility(View.GONE)
            }
            "CommunityFragment"->{
                transaction.add(R.id.fragment_container, CommunityFragment(), "CommunityFragment")
                transaction.addToBackStack(null)
                binding.floatingActionButton.show()
            }
            "ChattingFragment"->{
                transaction.add(R.id.fragment_container, ChattingFragment(), "ChattingFragment")
                transaction.addToBackStack(null)
                binding.floatingActionButton.hide()
            }
            "AccountFragment"->{
                    transaction.add(R.id.fragment_container, AccountFragment(), "AccountFragment")
                transaction.addToBackStack(null)
                binding.floatingActionButton.hide()
            }
            "WriteFragment"->{
                transaction.add(R.id.fragment_container, WriteFragment(), "WriteFragment")
                transaction.addToBackStack(null)
                hideNaviBarAndFloatingBtn()
            }

        }
        transaction.commit()
    }

    fun hideNaviBarAndFloatingBtn() {
        mainActivity.setBottomNavigationVisibility(View.GONE)
        binding.floatingActionButton.hide()
    }

    fun showNaviBarAndFloatingBtn() {
        mainActivity.setBottomNavigationVisibility(View.VISIBLE)
        binding.floatingActionButton.show()
    }


    fun openHomeDetailFragment(bundle: Bundle){
        globalHomeDetailFragment.arguments = bundle
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
