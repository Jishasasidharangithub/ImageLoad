package com.jisha.imageload.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.jisha.imageload.R


fun Context.ImagePickerDialog(listener: ImagePickerDialog) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setContentView(R.layout.dialog_image_picker)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val camera = dialog.findViewById<TextView>(R.id.tvCamera)
    val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
    val cancelButton = dialog.findViewById<TextView>(R.id.tvCancel)

    camera?.setOnClickListener {
        listener.onCamera()
        dialog.dismiss()
    }
    gallery?.setOnClickListener {
        listener.onGallery()
        dialog.dismiss()
    }
    cancelButton?.setOnClickListener {
        listener.onCancelled()
        dialog.dismiss()
    }
    dialog.show()
}

interface ImagePickerDialog {
    fun onGallery()
    fun onCamera()
    fun onCancelled()
}
