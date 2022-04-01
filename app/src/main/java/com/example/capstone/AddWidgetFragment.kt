package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.databinding.FragmentAddWidgetBinding


class AddWidgetFragment : Fragment() {

    private lateinit var addWidgetRecycler: AddWidgetRecycler

    private var _binding: FragmentAddWidgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddWidgetBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val widgetList = arrayOf("Weather Widget", "Time Widget", "News Widget", "Finance Widget", "Chart Widget", "MQTT Widget")
        val widgetIconList = arrayOf(R.drawable.ic_baseline_wb_sunny_24, R.drawable.ic_baseline_access_time_24, R.drawable.ic_baseline_article_24, R.drawable.ic_baseline_attach_money_24, R.drawable.ic_baseline_insert_chart_outlined_24, R.drawable.ic_baseline_wifi_24)

        addWidgetRecycler = AddWidgetRecycler(widgetList, widgetIconList)
        binding.recyclerView.adapter = addWidgetRecycler

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}