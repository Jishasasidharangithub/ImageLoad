package com.jisha.imageload.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.jisha.imageload.adapter.ImagesAdapter
import com.jisha.imageload.databinding.ActivityMainBinding
import com.jisha.imageload.modelclass.FileItem
import com.jisha.imageload.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File

class MainActivity : AppCompatActivity(), ImagesAdapter.OnItemClickListener, ImagePickerDialog {
    private val TAG = "ImageAddingActivity"
    private val invoiceFilesArray = ArrayList<FileItem>()
    private var binding: ActivityMainBinding? = null
    private var imagesAdapter: ImagesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
    }

    private fun init() {
        binding?.rvImages?.apply {
            setHasFixedSize(true)
            DefaultItemAnimator()
            layoutManager = GridLayoutManager(
                this@MainActivity,
                3
            )
        }
        imagesAdapter = ImagesAdapter(this)
        binding?.rvImages?.adapter = imagesAdapter
        onClickEvents()
    }

    private fun onClickEvents() {
        binding?.btSubmit?.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra("invoiceFilesArray", invoiceFilesArray)
            setResult(2, intent)
            finish()
        }
    }

    private fun openCamera() {
        if (hasCameraPermission()) {
            ImagePickerDialog(this)
        } else {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else shortToast("Permission is denied")
        }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setAllowFlipping(false)
            .setAllowCounterRotation(true)
            .setOutputCompressQuality(20)
            .start(this)
    }

    private val IMAGE_PICKER_REQUEST = 8547

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICKER_REQUEST -> {
                    val uri = data?.data!!
                    //logThis("Picked image Uri = $uri")
                    startCropImageActivity(uri)
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = getUriFromContentUri(this, result.uri, true).path ?: ""
                    printFileSizeInMb(resultUri)
                    handleUri(resultUri, Uri.fromFile(File(resultUri)))
                }
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            shortToast("Failed to crop image ${CropImage.getActivityResult(data).error}")

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onGallery() {
        ImagePicker.with(this)
            .galleryOnly()
            .start(IMAGE_PICKER_REQUEST)
    }

    override fun onCamera() {
        ImagePicker.with(this)
            .cameraOnly()
            .start(IMAGE_PICKER_REQUEST)
    }

    override fun onCancelled() {}

    private fun handleUri(uriFilePath: String?, originalUri: Uri) {

        Log.d("FILE_RESULT", "handlePdf: ${uriFilePath}")

        uriFilePath?.let {
            invoiceFilesArray.add(FileItem(uriFilePath, originalUri))
            imagesAdapter?.updateList(invoiceFilesArray)
            imagesAdapter?.notifyDataSetChanged()
            binding?.llLayoutEmpty?.hide()

        } ?: run {
            Log.e(TAG, "handleUri: Cancelled ")
        }
    }

    override fun onRemoveItem(item: FileItem) {
        invoiceFilesArray.remove(item)
        imagesAdapter?.updateList(invoiceFilesArray)
        imagesAdapter?.notifyDataSetChanged()
    }

    override fun onAddItem() {
        openCamera()
    }

}