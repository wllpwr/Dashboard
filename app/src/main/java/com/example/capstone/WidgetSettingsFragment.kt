package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class WidgetSettingsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var addWidgetRecycler: AddWidgetRecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragement_dashboard, container, false)

        recycler = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(context)

        val widgetSettings = arrayOf("Location", "C° or F°", "Theme", "Font", "Font Size")

        addWidgetRecycler = AddWidgetRecycler(widgetSettings)
        recycler.adapter = addWidgetRecycler

        return view
    }


}