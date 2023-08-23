package com.example.marketproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)

        init()
        clickListener()

        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
    }

    private fun clickListener(){
        binding.btnLogout.setOnClickListener {
            mainActivity.auth?.signOut()
            //mainActivity.setFragment("LoginFragment")
        }

    }
}