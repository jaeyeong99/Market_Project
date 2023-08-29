package com.example.marketproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.databinding.FragmentChattingBinding
import com.example.marketproject.databinding.FragmentChattingDetailBinding

class ChattingDetailFragment : Fragment() {

    private lateinit var binding: FragmentChattingDetailBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingDetailBinding.inflate(layoutInflater)

        init()

        return inflater.inflate(R.layout.fragment_chatting_detail, container, false)
    }

    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.binding.floatingActionButton.hide()
    }

}