package com.example.marketproject.fragment

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.marketproject.R
import com.example.marketproject.activity.MainActivity
import com.example.marketproject.data.HomeData
import com.example.marketproject.databinding.ActivityMainBinding
import com.example.marketproject.databinding.FragmentHomeBinding
import com.example.marketproject.databinding.FragmentWriteBinding
import com.example.marketproject.viewmodel.HomeViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger


class WriteFragment : Fragment() {

    private lateinit var binding: FragmentWriteBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var storage: FirebaseStorage

    var savedImageUri: Uri? = null

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteBinding.inflate(layoutInflater)

        init()
        clickListener()

        return binding.root
    }


    private fun init(){
        mainActivity = activity as MainActivity
        mainActivity.hideNaviBarAndFloatingBtn()
    }

    private fun clickListener(){
        binding.btnCamera.setOnClickListener {
            openGallery()
        }

        binding.btnCompleted.setOnClickListener {


            successWrite()
            //mainActivity.setFragment(HomeFragment(), "HomeFragment")
            mainActivity.replaceFragment("HomeFragment")

        }

    }

    private fun successWrite() {
        val database = Firebase.database
        val postsRef = database.reference.child("salesPost")
        val newChildReference = postsRef.push()
        val postInfoMap = mutableMapOf<String, Any>()


        val key = newChildReference.toString().replaceFirst("$postsRef/", "")
        //val key = newChildReference.toString()
        val id = mainActivity.auth?.currentUser?.uid.orEmpty()
        val title = binding.etTitle.text.toString()
        val price = binding.etPrice.text.toString()
        val description = binding.etDescription.text.toString()
        val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

        postInfoMap["key"] = key
        postInfoMap["id"] = id
        postInfoMap["title"] = title
        postInfoMap["price"] = price
        postInfoMap["description"] = description
        postInfoMap["timeStamp"] = timeStamp


        newChildReference.setValue(postInfoMap)

        var storageUrl = "gs://marketproject-29c48.appspot.com"
        storage = FirebaseStorage.getInstance(storageUrl)
        if (savedImageUri != null) {

            storage.reference.child("salesPostImage").child("$key")
                .putFile(savedImageUri!!)
        }
    }

    private fun openGallery() {
        // Create an intent to open the gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            val selectedImageUri: Uri? = data.data
            savedImageUri = selectedImageUri
            // Display the selected image
            binding.imageView.setImageURI(selectedImageUri)
            binding.imageView.setBackgroundResource(R.drawable.shape_round)
        }
    }





}