package com.jisha.imageload.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jisha.imageload.databinding.ItemImagesBinding
import com.jisha.imageload.modelclass.FileItem
import com.jisha.imageload.utils.hide
import com.jisha.imageload.utils.inVisible
import com.jisha.imageload.utils.show

class ImagesAdapter  (val listener: OnItemClickListener) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private var imageList = mutableListOf<FileItem>()

    inner class ViewHolder(val binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (imageList.isNotEmpty()) {
            if (imageList.size == position) {
                onAddItemView(holder, position)
            } else {
                holder.binding.ivItemImage.show()
                holder.binding.ivClose.show()
                holder.binding.ivAddImage.hide()
                with(imageList[position]) {
                    imageFilePath?.let {
                        try {
                            val bitmapImage = BitmapFactory.decodeFile(it)
                            val nh = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
                            val scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true)
                            holder.binding.ivItemImage.setImageBitmap(scaled)
                        } catch (e: Exception) {
                        }
                    }

                    holder.binding.ivClose.setOnClickListener {
                        listener.onRemoveItem(imageList[position])
                    }
                }
            }

        } else {
            onAddItemView(holder, position)
        }
    }

    private fun onAddItemView(holder: ViewHolder, position: Int) {
        holder.binding.ivItemImage.inVisible()
        holder.binding.ivClose.hide()
        holder.binding.ivAddImage.show()
        holder.itemView.setOnClickListener {
            if (imageList.size == position) {
                listener.onAddItem()
            }
        }
    }

    fun updateList(list: List<FileItem>) {
        imageList.clear()
        imageList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageList.size + 1
    }

    interface OnItemClickListener {
        fun onRemoveItem(item: FileItem)
        fun onAddItem()
    }
}