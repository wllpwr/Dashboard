package com.example.capstone

import androidx.lifecycle.ViewModel

class WidgetViewModel : ViewModel() {

    var widgetList = ArrayList<String>()

    var isStart = true

    val weatherWidget = "https://appassets.androidplatform.net/assets/WeatherWidget.html"
    val timeWidget = "https://appassets.androidplatform.net/assets/TimeWidget.html"
    val chartWidget = "https://appassets.androidplatform.net/assets/ChartWidget.html"
    val newsWidget = "https://appassets.androidplatform.net/assets/news.html"
    val financeWidget = "https://appassets.androidplatform.net/assets/stocks.html"
    val mqttWidget = "https://appassets.androidplatform.net/assets/mqtt.html"
}