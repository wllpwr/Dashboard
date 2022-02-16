package com.example.capstone

import androidx.lifecycle.ViewModel

class WidgetViewModel : ViewModel() {

    var widgetList = ArrayList<String>()

    val weatherWidget = "https://appassets.androidplatform.net/assets/WeatherWidget.html"
    val timeWidget = "https://appassets.androidplatform.net/assets/TimeWidget.html"
    val chartWidget = "https://appassets.androidplatform.net/assets/ChartWidget.html"
}