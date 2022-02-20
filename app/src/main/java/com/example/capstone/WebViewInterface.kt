package com.example.capstone

import android.webkit.JavascriptInterface
import android.content.Context
import android.util.Log
import java.io.File

class WebViewInterface(private val context: Context) {
    @JavascriptInterface
    fun createAndWriteToJson(fileName: String, jsonString: String) {
        Log.d("test1", fileName)
        Log.d("test1", jsonString)

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }

        val jsonString2 = context.openFileInput(fileName).bufferedReader().readText()

        val file = File(context.filesDir, fileName)

        Log.d("test1", file.canonicalPath)

        Log.d("test1", jsonString2)
    }
}