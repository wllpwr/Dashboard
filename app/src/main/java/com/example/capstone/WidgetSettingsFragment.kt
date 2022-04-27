package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.*
import org.json.JSONArray
import org.json.JSONTokener

class WidgetSettingsFragment : PreferenceFragmentCompat() {
    private val args: WidgetSettingsFragmentArgs by navArgs()
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val index = args.index

        val settingsFileName = widgetViewModel.widgetList[index].settingsFile
        val key = widgetViewModel.widgetList[index].key

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
                "list" -> {
                    val list = jsonArray.getJSONObject(i).getJSONArray("list")
                    var listArray = arrayOfNulls<String>(list.length())
                    for (j in 0 until list.length()) {
                        listArray[j] = list[j].toString()
                    }
                    preference = ListPreference(prefContext)

                    preference.entries = listArray
                    preference.entryValues = listArray

                }

            }

            preference.title = title
            preference.summary = summary
            preference.key = name + "_" + key

            screen.addPreference(preference)
        }


        // update or add widget button
        val confirmButton = Preference(prefContext)
        confirmButton.key = "ConfirmButton"
        confirmButton.title = getString(R.string.confirm)
        confirmButton.summary = getString(R.string.confirmWidgetSubtext)

        confirmButton.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { //code for what you want it to do
                findNavController().navigate(R.id.action_widgetSettingsFragment_to_dashboardFragment2)
                true
            }

        val themePref = ListPreference(prefContext)
        themePref.key = "theme_$key"
        themePref.title = getString(R.string.widget_app_theme)
        themePref.summary = getString(R.string.widget_theme_summary)
        themePref.entries = resources.getStringArray(R.array.theme)
        themePref.entryValues = resources.getStringArray(R.array.theme)

        val fontPref = ListPreference(prefContext)
        fontPref.key = "font_$key"
        fontPref.title = getString(R.string.widget_app_font)
        fontPref.summary = getString(R.string.widget_font_summary)
        fontPref.entries = resources.getStringArray(R.array.font)
        fontPref.entryValues = resources.getStringArray(R.array.font)

        screen.addPreference(themePref)
        screen.addPreference(fontPref)
        screen.addPreference(confirmButton)

        preferenceScreen = screen
    }

    private fun readJson(fileName: String) : String? {
        return context?.openFileInput(fileName)?.bufferedReader()?.readText()
    }
}