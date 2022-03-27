package com.example.capstone

import androidx.lifecycle.ViewModel

class WidgetViewModel : ViewModel() {

    var widgetList = ArrayList<String>()
    var settingsList = ArrayList<String>()
    var keyList = ArrayList<String>()

    var isStart = true
    var isMove = false

    val weatherWidget = "WeatherWidget.html"

    val timeWidget = "TimeWidget.html"
    val chartWidget = "ChartWidget.html"
    val newsWidget = "reddit.html"
    val financeWidget = "stocks.html"
    val mqttWidget = "mqtt.html"

    val path = "https://appassets.androidplatform.net/widget/"
}