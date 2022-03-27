package com.example.capstone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commit


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: WidgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}