package com.example.capstone

import android.webkit.JavascriptInterface
import android.content.Context

class WebViewInterface(private val context: Context) {
    @JavascriptInterface
    fun createAndWriteToJson(fileName: String, jsonString: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    @JavascriptInterface
    fun readJson(fileName: String) : String {
        return context.openFileInput("weatherSettings.json").bufferedReader().readText()
    }

}