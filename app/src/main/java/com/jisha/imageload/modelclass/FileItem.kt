package com.jisha.imageload.modelclass

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class FileItem(
    var imageFilePath: String? = null,
    var imageUri: Uri? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageFilePath)
        parcel.writeParcelable(imageUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileItem> {
        override fun createFromParcel(parcel: Parcel): FileItem {
            return FileItem(parcel)
        }

        override fun newArray(size: Int): Array<FileItem?> {
            return arrayOfNulls(size)
        }
    }
}
