package com.example.capstone

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat

@SuppressLint("SetJavaScriptEnabled")
class RecyclerGrid(private var dataList: ArrayList<String>): RecyclerView.Adapter<RecyclerGrid.ViewHolder>() {


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

        // Set item views based on your views and data model
        holder.webView.settings.javaScriptEnabled = true
        holder.webView.settings.allowFileAccess = true

        holder.webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Log.d("test1", "${message.message()} -- From line " +
                        "${message.lineNumber()} of ${message.sourceId()}")
                return true
            }
        }

        val json = holder.webView.context.openFileInput("weatherSettings.json").bufferedReader().readText()

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setCookie(data, json)

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(holder.webView.context))
            .build()
        holder.webView.webViewClient = LocalContentWebViewClient(assetLoader)

        holder.webView.loadUrl(data)
    }

    override fun getItemCount() = dataList.size
}