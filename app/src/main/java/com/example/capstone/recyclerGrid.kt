package com.example.capstone

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat


class RecyclerGrid: RecyclerView.Adapter<RecyclerGrid.ViewHolder>() {

    private var dataList = ArrayList<String>()

    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        //@RequiresApi(21)
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }

    fun moveItem(from: Int, to: Int) {
        var fromWidget = dataList[from]
        var toWidget = dataList[to]
        val tempFromWidget = dataList[from]

        fromWidget = toWidget
        toWidget = tempFromWidget
    }

    internal fun setDataList(dataList: ArrayList<String>) {
        this.dataList = dataList
    }

    fun deleteItem(index: Int) {
        dataList.removeAt(index)
        notifyDataSetChanged() // crashes with position update???
    }

    /*
    fun addWidget(widget: String) {
        dataList.add(widget)
        notifyItemChanged(dataList.size)
    }
     */

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

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(holder.webView.context))
            .build()
        holder.webView.webViewClient = LocalContentWebViewClient(assetLoader)

        holder.webView.loadUrl(data)
    }

    override fun getItemCount() = dataList.size

}