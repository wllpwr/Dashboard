package com.example.capstone

import android.annotation.SuppressLint
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
import androidx.webkit.WebViewAssetLoader
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.webkit.WebViewClientCompat
import kotlinx.android.synthetic.main.widget.view.*

@SuppressLint("SetJavaScriptEnabled")
class WidgetSettingsFragment : Fragment() {

    private val args: WidgetSettingsFragmentArgs by navArgs()
    private val widgetViewModel: WidgetViewModel by activityViewModels()
    private lateinit var addWidgetButton: Button
    private lateinit var webView: WebView

    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }

    private fun updateNative(){
        webView.evaluateJavascript(
            "updateFromNative()",null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_widget_settings, container, false)

        webView = view.findViewById(R.id.web_view)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebViewInterface(webView.context),"Android")

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(view.context))
            .build()
        webView.webViewClient = LocalContentWebViewClient(assetLoader)

        addWidgetButton = view.findViewById(R.id.add_widget_button)

        val widget = args.widgetType

        addWidgetButton.setOnClickListener {
            when (widget) { // ADD WIDGET
                '*' + widgetViewModel.weatherWidget -> {
                    updateNative()
                    view.findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                }
                "Weather Widget" -> {
                    updateNative()

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
        }
        when (widget) {
            // WIDGET SETTINGS
            '*' + widgetViewModel.weatherWidget -> {
                view.web_view.loadUrl(widgetViewModel.weatherWidgetSettings)
            }
            "Weather Widget" -> {
                view.web_view.loadUrl(widgetViewModel.weatherWidgetSettings)
            }
            '*' + widgetViewModel.timeWidget -> {
                view.web_view.loadUrl(widgetViewModel.timeWidget)
            }
            "Time Widget" -> {
                view.web_view.loadUrl(widgetViewModel.timeWidget)
            }
            '*' + widgetViewModel.chartWidget -> {
                view.web_view.loadUrl(widgetViewModel.chartWidget)
            }
            "Chart Widget" -> {
                view.web_view.loadUrl(widgetViewModel.chartWidget)
            }
            '*' + widgetViewModel.newsWidget -> {
                view.web_view.loadUrl(widgetViewModel.newsWidget)
            }
            "News Widget" -> {
                view.web_view.loadUrl(widgetViewModel.newsWidget)
            }
            '*' + widgetViewModel.financeWidget -> {
                view.web_view.loadUrl(widgetViewModel.financeWidget)
            }
            "Finance Widget" -> {
                view.web_view.loadUrl(widgetViewModel.financeWidget)
            }
            '*' + widgetViewModel.mqttWidget -> {
                view.web_view.loadUrl(widgetViewModel.mqttWidget)
            }
            "MQTT Widget" -> {
                view.web_view.loadUrl(widgetViewModel.mqttWidget)
            }
            else -> {
                Log.d("addWidgetButton", "else")
            }
        }
        return view
    }
}
