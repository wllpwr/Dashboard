package com.example.capstone

import android.R.attr.button
import android.os.Bundle
import android.webkit.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.*
import kotlinx.android.synthetic.main.widget.view.*
import org.json.JSONArray
import org.json.JSONTokener


class WidgetSettingsFragment : PreferenceFragmentCompat() {
    private val args: WidgetSettingsFragmentArgs by navArgs()
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val widget = args.widgetType

        val prefContext = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(prefContext)

        var preference = Preference(prefContext)


        val jsonString = readJson(widget)

        val keySuffix = generateKey()

        val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val name = jsonArray.getJSONObject(i).getString("name")
            val type = jsonArray.getJSONObject(i).getString("type")
            val title = jsonArray.getJSONObject(i).getString("title")
            val summary = jsonArray.getJSONObject(i).getString("summary")

            if (type  == "editText") {
                preference = EditTextPreference(prefContext)
            }
            if (type == "switch") {
                preference = SwitchPreferenceCompat(prefContext)
            }

            preference.title = title
            preference.summary = summary
            preference.key = name + keySuffix

            screen.addPreference(preference)
        }



        // update or add widget button
        val addOrUpdateButton = Preference(prefContext)
        addOrUpdateButton.key = "ConfirmButton"
        addOrUpdateButton.title = "Confirm"
        addOrUpdateButton.summary = "Update Settings of Widget or Add Widget"

        addOrUpdateButton.onPreferenceClickListener = Preference.OnPreferenceClickListener { //code for what you want it to do
            addWidget(widget)
        }


        screen.addPreference(addOrUpdateButton)

        preferenceScreen = screen
    }

    private fun addWidget(widget: String) : Boolean {
        when (widget) { // ADD WIDGET
            '*' + widgetViewModel.weatherWidget -> {
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            "Weather Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.weatherWidget)
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            "Time Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.timeWidget)
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            "Chart Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.chartWidget)
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            "News Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.newsWidget)
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            "Finance Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.financeWidget)
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            "MQTT Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.mqttWidget)
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
            else -> {
                view?.findNavController()?.navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                return true
            }
        }
    }


    private fun readJson(fileName: String) : String? {
        return context?.openFileInput("weatherSettings.json")?.bufferedReader()?.readText()
    }

    private fun generateKey() : String {
        val keySize = 10

        var key = "_"

        val alphaNumerics = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        for (i in 1..keySize) {
            val index = (alphaNumerics.indices).random()

            key += alphaNumerics[index]
        }

        return key
    }
}

/*
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

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Log.d("test1", "${message.message()} -- From line " +
                        "${message.lineNumber()} of ${message.sourceId()}")
                return true
            }
        }

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
*/