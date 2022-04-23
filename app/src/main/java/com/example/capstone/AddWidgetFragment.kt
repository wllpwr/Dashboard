package com.example.capstone

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.databinding.FragmentAddWidgetBinding
import java.io.File


class AddWidgetFragment : Fragment() {

    private lateinit var addWidgetRecycler: AddWidgetRecycler
    private lateinit var downloadManager: DownloadManager
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    private var _binding: FragmentAddWidgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddWidgetBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        downloadManager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val widgetList = arrayOf("Weather Widget", "Time Widget", "Reddit Widget", "Stocks Widget")
        val widgetIconList = arrayOf(R.drawable.ic_baseline_wb_sunny_24, R.drawable.ic_baseline_access_time_24, R.drawable.ic_baseline_article_24, R.drawable.ic_baseline_attach_money_24)

        addWidgetRecycler = AddWidgetRecycler(widgetList, widgetIconList)
        binding.recyclerView.adapter = addWidgetRecycler

        binding.customWidgetButton.setOnClickListener {
            val key = generateKey()

            val request =
                DownloadManager.Request(Uri.parse("https://raw.githubusercontent.com/wllpwr/Dashboard/master/app/src/main/assets/stocks.html"))
                    .setTitle("Custom Widget Downloaded")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "CustomWidget_${key}.html")
            downloadManager.enqueue(request)

            val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val htmlFile = File(file.absolutePath.toString() + "/CustomWidget_${key}.html")

            widgetViewModel.widgetList.add(Widget("file://" + htmlFile.absolutePath, "stocksSettings.json", key))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateKey() : String {
        val keySize = 10

        var key = ""

        val alphaNumerics = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        for (i in 1..keySize) {
            val index = (alphaNumerics.indices).random()

            key += alphaNumerics[index]
        }

        return key
    }
}