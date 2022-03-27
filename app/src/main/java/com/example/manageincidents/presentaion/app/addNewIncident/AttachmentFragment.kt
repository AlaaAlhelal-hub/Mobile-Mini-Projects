package com.example.manageincidents.presentaion.app.addNewIncident

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.manageincidents.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttachmentFragment : Fragment() {


    private lateinit var imagePreview: ImageView
    private lateinit var deleteIcon: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.row_attachement, container, false )
        imagePreview = inflate.findViewById(R.id.attachment_imageView)
        deleteIcon = inflate.findViewById(R.id.deleteAttachment)
        deleteIcon.setOnClickListener {
            deleteImage()
        }
        return inflate
    }

    private fun deleteImage(){
        imagePreview.visibility = View.GONE
        deleteIcon.visibility = View.GONE
    }
    
}