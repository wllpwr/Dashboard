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
                putString("$count" + "widget", widget)
                putString("$count" + "settings", viewModel.settingsList[count])
                putString("$count" + "key", viewModel.keyList[count])
            }
            apply()
            Log.d("test", "onStopSharedPref" + sharedPref.all.toString())
            Log.d("test", "onStopViewModel" + viewModel.widgetList.toString())
        }

        super.onStop()
    }

    private fun changeTheme(theme: String, font: String) {
        if (theme == "Blue" && font == "Roboto") {
            setTheme(R.style.ThemeBlueRoboto)
            binding.textViewToTest.text = R.style.ThemeBlueRoboto.toString()
        } else if (theme == "Blue" && font == "Roboto Mono") {
            setTheme(R.style.ThemeBlueRobotoMono)
            binding.textViewToTest.text = R.style.ThemeBlueRobotoMono.toString()
        } else if (theme == "Black" && font == "Roboto") {
            setTheme(R.style.ThemeBlackRoboto)
            binding.textViewToTest.text = R.style.ThemeBlackRoboto.toString()
        } else if (theme == "Black" && font == "Roboto Mono") {
            setTheme(R.style.ThemeBlackRobotoMono)
            binding.textViewToTest.text = R.style.ThemeBlackRobotoMono.toString()
        } else if (theme == "Nord" && font == "Roboto") {
            setTheme(R.style.ThemeNordRoboto)
            binding.textViewToTest.text = R.style.ThemeNordRoboto.toString()
        } else if (theme == "Nord" && font == "RobotoMono") {
            setTheme(R.style.ThemeNordRobotoMono)
            binding.textViewToTest.text = R.style.ThemeNordRobotoMono.toString()
        } else if (theme == "Solarized Light" && font == "Roboto") {
            setTheme(R.style.ThemeSolarizedLightRoboto)
            binding.textViewToTest.text = R.style.ThemeSolarizedLightRoboto.toString()
        } else if (theme == "Solarized Light" && font == "Roboto Mono") {
            setTheme(R.style.ThemeSolarizedRobotoMono)
            binding.textViewToTest.text = R.style.ThemeSolarizedRobotoMono.toString()
        } else if (theme == "Arc" && font == "Roboto") {
            setTheme(R.style.ThemeArcRoboto)
            binding.textViewToTest.text = R.style.ThemeArcRoboto.toString()
        } else if (theme == "Arc" && font == "Roboto Mono") {
            setTheme(R.style.ThemeArcRobotoMono)
            binding.textViewToTest.text = R.style.ThemeArcRobotoMono.toString()
        }

    }


}