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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

    var currentFragment : String? = null
    var backPressedTime : Long = 0

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed : $currentFragment")

            when(currentFragment) {
                "WriteFragment" -> {
                    setFragment(HomeFragment(), "HomeFragment")
                }
                "HomeDetailFragment" -> {
                    setFragment(HomeFragment(), "HomeFragment")
                }
                "SignUpFragment" -> {
                    setFragment(LoginFragment(), "LoginFragment")
                }
                else -> {
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                    }
                    backPressedTime = System.currentTimeMillis()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        mainActivity = this

        auth = Firebase.auth
        currentUser = auth?.currentUser

        setContentView(binding.root)

        this.onBackPressedDispatcher.addCallback(this, callback)



        binding.bottomNavigationView.run {
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.item_home_fragment -> {
                        setFragment(HomeFragment(), "HomeFragment")
                    }
                    R.id.item_community_fragment -> {
                        setFragment(CommunityFragment(), "CommunityFragment")
                    }
                    R.id.item_chatting_fragment -> {
                        setFragment(ChattingFragment(), "ChattingFragment")
                    }
                    R.id.item_account_fragment -> {
                        setFragment(AccountFragment(), "AccountFragment")
                    }
                }
                true
            }
        }

        if (currentUser != null) {
            setFragment(HomeFragment(), "HomeFragment")
            Log.d(TAG, "시작")
            showNaviBarAndFloatingBtn()
        } else {
            setFragment(LoginFragment(), "LoginFragment")
            hideNaviBarAndFloatingBtn()
        }

        binding.floatingActionButton.setOnClickListener {
            if(supportFragmentManager.findFragmentByTag("HomeFragment") is HomeFragment) {
                setFragment(WriteFragment(), "WriteFragment")
            }
        }



    }



    fun setFragment(fragment: Fragment, tag: String){
        val manager: FragmentManager = supportFragmentManager
        val transaction = manager.beginTransaction()

        if (tag == "HomeFragment") {
            when (currentFragment) {
                "LoginFragment" -> {
                    transaction.remove(manager.findFragmentByTag("LoginFragment")!!)
                }
                "WriteFragment" -> {
                    transaction.remove(manager.findFragmentByTag("WriteFragment")!!)
                }
                "HomeDetailFragment" -> {
                    transaction.remove(manager.findFragmentByTag("HomeDetailFragment")!!)
                }
            }
        }



        if (manager.findFragmentByTag(tag) == null) {
            transaction.add(R.id.fragment_container, fragment, tag)
            currentFragment = tag
        } else {

            val home = manager.findFragmentByTag("HomeFragment")
            val community = manager.findFragmentByTag("CommunityFragment")
            val chatting = manager.findFragmentByTag("ChattingFragment")
            val account = manager.findFragmentByTag("AccountFragment")

            if (home!= null) {
                transaction.hide(home)
            }
            if (community!= null) {
                transaction.hide(community)
            }
            if (chatting!= null) {
                transaction.hide(chatting)
            }
            if (account!= null) {
                transaction.hide(account)
            }

            if(tag == "HomeFragment") {
                if (home != null) {

                    transaction.show(home)
                    currentFragment = tag
                    showNaviBarAndFloatingBtn()
                }
            } else if (tag == "CommunityFragment") {
                if (community != null) {
                    transaction.show(community)
                    currentFragment = tag
                }
            } else if (tag == "ChattingFragment") {
                if (chatting != null) {
                    transaction.show(chatting)
                    currentFragment = tag
                    binding.floatingActionButton.hide()
                }
            } else if (tag == "AccountFragment") {
                if (account != null) {
                    transaction.show(account)
                    currentFragment = tag
                    binding.floatingActionButton.hide()
                }
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
        setFragment(globalHomeDetailFragment, "HomeDetailFragment")
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

    fun replaceFragment(tag: String) {

        val manager: FragmentManager = supportFragmentManager
        val transaction = manager.beginTransaction()
        when(tag){
//            "LoginFragment"->{
//                transaction.add(R.id.fragment_container, LoginFragment(), "LoginFragment")
//                mainActivity.setBottomNavigationVisibility(View.GONE)
//                //supportActionBar?.hide()
//            }
//            "SignUpFragment"->{
//                transaction.add(R.id.fragment_container, LoginFragment(), "SignUpFragment")
//                transaction.addToBackStack(null)
//                mainActivity.setBottomNavigationVisibility(View.GONE)
//                //supportActionBar?.hide()
//            }
            "HomeFragment"->{
                transaction.replace(R.id.fragment_container, HomeFragment(), "HomeFragment")
                currentFragment = tag
                showNaviBarAndFloatingBtn()
            }
//            "HomeDetailFragment"->{
//                transaction.add(R.id.fragment_container, globalHomeDetailFragment, "HomeDetailFragment")
//                hideNaviBarAndFloatingBtn()
//            }
//            "CommunityFragment"->{
//                transaction.add(R.id.fragment_container, CommunityFragment(), "CommunityFragment")
//                transaction.addToBackStack(null)
//                binding.floatingActionButton.show()
//            }
//            "ChattingFragment"->{
//                transaction.add(R.id.fragment_container, ChattingFragment(), "ChattingFragment")
//                transaction.addToBackStack(null)
//                binding.floatingActionButton.hide()
//            }
//            "AccountFragment"->{
//                transaction.add(R.id.fragment_container, AccountFragment(), "AccountFragment")
//                transaction.addToBackStack(null)
//                binding.floatingActionButton.hide()
//            }
//            "WriteFragment"->{
//                transaction.replace(R.id.fragment_container, WriteFragment(), "WriteFragment")
//                transaction.addToBackStack(null)
//                hideNaviBarAndFloatingBtn()
//            }

        }
        transaction.commit()
    }


}
