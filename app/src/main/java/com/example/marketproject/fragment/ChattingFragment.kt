package com.example.marketproject.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.databinding.FragmentChattingBinding

class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(layoutInflater)

        init()
        clickListener()
        Log.d(TAG, "messageclick")

        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.binding.floatingActionButton.hide()
    }

    private fun clickListener(){
        binding.tvTitle.setOnClickListener {
            mainActivity.setFragment(ChattingDetailFragment(), "ChattingDetailFragment")
            mainActivity.hideNaviBarAndFloatingBtn()
        }
    }
}