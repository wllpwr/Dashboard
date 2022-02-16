package com.example.capstone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs


class WidgetSettingsFragment : Fragment() {

    private val args: WidgetSettingsFragmentArgs by navArgs()
    private val widgetViewModel: WidgetViewModel by activityViewModels()
    private lateinit var addWidgetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_widget_settings, container, false)

        val addWidgetButton = view.findViewById<Button>(R.id.add_widget_button)

        val widget = args.widgetType

        addWidgetButton.setOnClickListener {
            when (widget) {
                "Weather Widget" -> {
                    widgetViewModel.widgetList.add(widgetViewModel.weatherWidget)
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                "Time Widget" -> {
                    widgetViewModel.widgetList.add(widgetViewModel.timeWidget)
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                "Chart Widget" -> {
                    widgetViewModel.widgetList.add(widgetViewModel.chartWidget)
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                else -> {
                    Log.d("addWidgetButton", "else")
                }
            }
        }

        return view
    }


}