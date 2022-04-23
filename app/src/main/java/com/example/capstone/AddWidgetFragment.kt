package com.example.capstone

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.databinding.FragmentAddWidgetBinding
import java.io.File
import java.io.FileWriter


class AddWidgetFragment : Fragment() {

    private lateinit var addWidgetRecycler: AddWidgetRecycler
    private lateinit var downloadManager: DownloadManager
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    private var _binding: FragmentAddWidgetBinding? = null
    private val binding get() = _binding!!

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val key = generateKey()

                context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

                val file = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Custom_Widget")

                val fileContent = file.bufferedReader().readText()

                file.delete()

                val customWidgetsDir = File(context?.filesDir, "custom_widgets")
                if(!customWidgetsDir.exists()) {
                    customWidgetsDir.mkdir()
                }

                try {
                    val customWidgetFile = File(customWidgetsDir, "$key.html")
                    val writer = FileWriter(customWidgetFile)
                    writer.write(fileContent)
                    writer.flush()
                    writer.close()
                } catch (e: Exception) {

                }

                widgetViewModel.widgetList.add(Widget("https://appassets.androidplatform.net/custom_widgets/${key}.html", "${key}.json", key))

                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()

                val navAction = AddWidgetFragmentDirections.actionAddWidgetFragmentToDashboardFragment2("custom")

                view?.findNavController()?.navigate(navAction)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddWidgetBinding.inflate(inflater, container, false)
        val view = binding.root
        context?.registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

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
                    .setTitle("Custom_Widget")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_MOBILE or
                                DownloadManager.Request.NETWORK_WIFI
                    )
                    .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "Custom_Widget")

            downloadManager.enqueue(request)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        context?.unregisterReceiver(broadcastReceiver)
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