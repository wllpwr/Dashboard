package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AddWidgetFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var addWidgetRecycler: AddWidgetRecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_widget, container, false)

        recycler = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(context)

        val widgetList = arrayOf("Weather Widget", "Time Widget", "News Widget", "Finance Widget")
        val widgetIconList = arrayOf(R.drawable.ic_baseline_wb_sunny_24, R.drawable.ic_baseline_access_time_24, R.drawable.ic_baseline_article_24, R.drawable.ic_baseline_attach_money_24)

        addWidgetRecycler = AddWidgetRecycler(widgetList, widgetIconList)
        recycler.adapter = addWidgetRecycler

        return view
    }


}