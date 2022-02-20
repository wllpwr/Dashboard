package com.example.capstone

import android.webkit.JavascriptInterface
import android.content.Context
import android.util.Log

class WebViewInterface(private val context: Context) {
    @JavascriptInterface
    fun createAndWriteToJson(fileName: String, jsonString: String) {
        Log.d("test1", fileName)
        Log.d("test1", jsonString)

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }

        val jsonString2 = context.openFileInput(fileName).bufferedReader().readText()

        Log.d("test1", jsonString2)
    }

    @JavascriptInterface
    fun logToAndroidFromWebView(message: String) {
        Log.d("test1", message)
    }


}