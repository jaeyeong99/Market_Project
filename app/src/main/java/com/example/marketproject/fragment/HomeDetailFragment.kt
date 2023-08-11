package com.example.marketproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.databinding.FragmentHomeDetailBinding

class HomeDetailFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailBinding
    private lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeDetailBinding.inflate(layoutInflater)

        init()


        return binding.root
    }

    private fun init(){
        mainActivity = activity as MainActivity
    }
}