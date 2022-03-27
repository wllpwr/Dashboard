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
import java.io.File


@SuppressLint("SetJavaScriptEnabled")
class RecyclerGrid(private var dataList: ArrayList<String>, private var settingsList: ArrayList<String>, private var keyList: ArrayList<String>, private var context: Context): RecyclerView.Adapter<RecyclerGrid.ViewHolder>() {


    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        //@RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
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
        // Get the data model based on position
        val data = dataList[position]
        val key = keyList[position]

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

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val preferences = sharedPrefs.all

        val keyValueOfPreference = preferences.filterKeys { it.contains(key) }

        for (keyValue in keyValueOfPreference) {
            val keyValueArray = keyValue.toString().split("=")
            val newKey = keyValueArray[0].split("_")[0]
            val cookie = newKey + "=" + keyValueArray[1]
            CookieManager.getInstance().setCookie(data, cookie)
        }

        val widgetDir = File(context.filesDir, "widget")
        if (!widgetDir.exists()) {
            widgetDir.mkdir()
        }
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", AssetsPathHandler(context))
            .addPathHandler("/widget/", WebViewAssetLoader.InternalStoragePathHandler(context, widgetDir))
            .build()
        holder.webView.webViewClient = LocalContentWebViewClient(assetLoader)

        holder.webView.loadUrl(data)
    }

    override fun getItemCount() = dataList.size
}