package com.example.capstone

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler
import androidx.webkit.WebViewClientCompat


@SuppressLint("SetJavaScriptEnabled")
class RecyclerGrid(private var dataList: ArrayList<String>, private var settingsList: ArrayList<String>, private var keyList: ArrayList<String>, private var context: Context): RecyclerView.Adapter<RecyclerGrid.ViewHolder>() {


    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader, private val widgetSettings: String) : WebViewClientCompat() {
        //@RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            view?.evaluateJavascript("javascript:getData('$widgetSettings');", null)
        }
    }

    fun deleteItem(index: Int) {
        dataList.removeAt(index)
        keyList.removeAt(index)
        settingsList.removeAt(index)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var webView: WebView = itemView.findViewById(R.id.web_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.widget, parent, false)

        //https://stackoverflow.com/questions/35221566/how-to-set-the-height-of-an-item-row-in-gridlayoutmanager
        view.post {
            view.layoutParams.height = parent.width/2
            view.requestLayout()
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set item views based on your views and data model
        holder.webView.settings.javaScriptEnabled = true
        holder.webView.addJavascriptInterface(WebViewInterface(holder.webView.context), "Android")

        holder.webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Log.d(
                    "test2", "${message.message()} -- From line " +
                            "${message.lineNumber()}"
                )
                return true
            }
        }

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", AssetsPathHandler(context))
            .build()
        holder.webView.webViewClient = LocalContentWebViewClient(assetLoader, getWidgetSettings(keyList[position]))

        holder.webView.loadUrl(dataList[position])
    }

    override fun getItemCount() = dataList.size

    private fun getWidgetSettings(key: String): String {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val preferences = sharedPrefs.all

        val keyValueOfPreference = preferences.filterKeys { it.contains(key) }

        var widgetData = ""
        for (keyValue in keyValueOfPreference) {
            val keyValueArray = keyValue.toString().split("=")
            val newKey = keyValueArray[0].split("_")[0]
            widgetData += newKey + "=" + keyValueArray[1] + ", "
        }

        return widgetData
    }
}