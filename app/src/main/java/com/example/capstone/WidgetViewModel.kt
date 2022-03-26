package com.example.capstone

import androidx.lifecycle.ViewModel

class WidgetViewModel : ViewModel() {

    var widgetList = ArrayList<String>()
    var settingsList = ArrayList<String>()
    var keyList = ArrayList<String>()

    var isStart = true
    var isMove = false

    val weatherWidget = "https://appassets.androidplatform.net/assets/WeatherWidget.html"

    val timeWidget = "https://appassets.androidplatform.net/assets/TimeWidget.html"
    val chartWidget = "https://appassets.androidplatform.net/assets/ChartWidget.html"
    val newsWidget = "https://appassets.androidplatform.net/assets/reddit.html"
    val financeWidget = "https://appassets.androidplatform.net/assets/stocks.html"
    val mqttWidget = "https://appassets.androidplatform.net/assets/mqtt.html"
}