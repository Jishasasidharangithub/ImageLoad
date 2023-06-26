package com.jisha.imageload.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.jisha.imageload.BuildConfig
import java.io.File
import java.text.CharacterIterator
import java.text.StringCharacterIterator

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}
fun Context.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun printFileSizeInMb(path: String): String {
    if (BuildConfig.DEBUG) {
        val file = File(path)
        var bytes = file.length()
        if (-1000 < bytes && bytes < 1000) {
            return "$bytes B"
        }
        val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
        while (bytes <= -999950 || bytes >= 999950) {
            bytes /= 1000
            ci.next()
        }
        val size =String.format("%.1f %cB", bytes / 1000.0, ci.current())
        //logThis(size)
        return size
    }
    return ""
}