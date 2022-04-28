package com.example.capstone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import com.example.capstone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val viewModel: WidgetViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        val theme = PreferenceManager.getDefaultSharedPreferences(this).all["theme"]
        val font = PreferenceManager.getDefaultSharedPreferences(this).all["font_style"]

        if (theme != null && font != null) {
            changeTheme(theme as String, font as String)
        }

        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
            }
        }
    }

    override fun onStop() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            clear()
            for ((count, widget) in viewModel.widgetList.withIndex()) {
                putString("$count" + "widget", widget.widgetUrl)
                putString("$count" + "settings", widget.settingsFile)
                putString("$count" + "key", widget.key)
            }
            apply()
            viewModel.widgetList.clear()
        }

        super.onStop()
    }

    private fun changeTheme(theme: String, font: String) {
        if (theme == "Blue" && font == "Roboto") {
            setTheme(R.style.ThemeBlueRoboto)
        } else if (theme == "Blue" && font == "Roboto Mono") {
            setTheme(R.style.ThemeBlueRobotoMono)
        } else if (theme == "Black" && font == "Roboto") {
            setTheme(R.style.ThemeBlackRoboto)
        } else if (theme == "Black" && font == "Roboto Mono") {
            setTheme(R.style.ThemeBlackRobotoMono)
        } else if (theme == "Nord" && font == "Roboto") {
            setTheme(R.style.ThemeNordRoboto)
        } else if (theme == "Nord" && font == "Roboto Mono") {
            setTheme(R.style.ThemeNordRobotoMono)
        } else if (theme == "Solarized Light" && font == "Roboto") {
            setTheme(R.style.ThemeSolarizedLightRoboto)
        } else if (theme == "Solarized Light" && font == "Roboto Mono") {
            setTheme(R.style.ThemeSolarizedRobotoMono)
        } else if (theme == "Arc" && font == "Roboto") {
            setTheme(R.style.ThemeArcRoboto)
        } else if (theme == "Arc" && font == "Roboto Mono") {
            setTheme(R.style.ThemeArcRobotoMono)
        }
    }


}