package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.*
import kotlinx.android.synthetic.main.fragment_widget_settings.*
import org.json.JSONArray
import org.json.JSONTokener

class WidgetSettingsFragment : PreferenceFragmentCompat() {
    private val args: WidgetSettingsFragmentArgs by navArgs()
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val index = args.index

        val widget = widgetViewModel.widgetList[index]
        val settingsFileName = widgetViewModel.settingsList[index]
        val key = widgetViewModel.keyList[index]

        val prefContext = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(prefContext)

        var preference = Preference(prefContext)

        val jsonString = readJson(settingsFileName)

        val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
        for (i in 0 until jsonArray.length()) {
            val name = jsonArray.getJSONObject(i).getString("name")
            val type = jsonArray.getJSONObject(i).getString("type")
            val title = jsonArray.getJSONObject(i).getString("title")
            val summary = jsonArray.getJSONObject(i).getString("summary")


            when(type) {
                "editText" -> preference = EditTextPreference(prefContext)
                "switch" -> preference = SwitchPreferenceCompat(prefContext)
            }

            preference.title = title
            preference.summary = summary
            preference.key = name + key

            screen.addPreference(preference)
        }



        // update or add widget button
        val confirmButton = Preference(prefContext)
        confirmButton.key = "ConfirmButton"
        confirmButton.title = "Confirm"
        confirmButton.summary = "Update Settings of Widget"

        confirmButton.onPreferenceClickListener = Preference.OnPreferenceClickListener { //code for what you want it to do
            findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
            true
        }


        screen.addPreference(confirmButton)

        preferenceScreen = screen
    }

    private fun readJson(fileName: String) : String? {
        return context?.openFileInput(fileName)?.bufferedReader()?.readText()
    }
}