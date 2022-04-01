package com.example.capstone

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


class AppSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        val button: Preference? = findPreference("ConfirmButton")

        button?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            //code for what you want it to do
            activity?.recreate()
            findNavController().navigate(R.id.action_appSettingsFragment_to_dashboardFragment2)
            true
        }
    }

}