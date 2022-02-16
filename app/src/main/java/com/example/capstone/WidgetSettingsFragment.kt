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

        addWidgetButton = view.findViewById(R.id.add_widget_button)

        val widget = args.widgetType

        addWidgetButton.setOnClickListener {
            when (widget) { // ADD WIDGET
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
                "News Widget" -> {
                    widgetViewModel.widgetList.add(widgetViewModel.newsWidget)
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                "Finance Widget" -> {
                    widgetViewModel.widgetList.add(widgetViewModel.financeWidget)
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                "MQTT Widget" -> {
                    widgetViewModel.widgetList.add(widgetViewModel.mqttWidget)
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                else -> {
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
            }
        }


        if (widget[0] == '*') {
            addWidgetButton.setText(R.string.apply_settings)
            when (widget) {
                // WIDGET SETTINGS
                "*" + widgetViewModel.weatherWidget -> {
                    Log.d("addWidgetButton", "weather widget settings")
                    return view
                }
                "*" + widgetViewModel.timeWidget -> {
                    Log.d("addWidgetButton", "time widget settings")
                    return view
                }
                "*" + widgetViewModel.chartWidget -> {
                    Log.d("addWidgetButton", "chart widget settings")
                    return view
                }
                "*" + widgetViewModel.newsWidget -> {
                    Log.d("addWidgetButton", "news widget settings")
                    return view
                }
                "*" + widgetViewModel.financeWidget -> {
                    Log.d("addWidgetButton", "finance widget settings")
                    return view
                }
                "*" + widgetViewModel.mqttWidget -> {
                    Log.d("addWidgetButton", "mqtt widget settings")
                    return view
                }
                else -> {
                    Log.d("addWidgetButton", "else")
                    return view
                }
            }
        }
        return view
    }
}
