package com.example.capstone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.databinding.FragementDashboardBinding
import java.util.*
import java.util.stream.IntStream.range

class DashboardFragment : Fragment() {
    private lateinit var  recyclerGridAdapter: RecyclerGrid
    private lateinit var gridLayoutManager: GridLayoutManager
    private val args: WidgetSettingsFragmentArgs by navArgs()
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    private lateinit var swipeHelper: ItemTouchHelper

    private lateinit var dragHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        val widgetType = args.widgetType

        if (widgetType != "none") {
            addWidget(widgetType)
        }

        //https://stackoverflow.com/questions/17401799/how-to-know-how-many-shared-preference-in-shared-preferences-in-android/17401994
        if (widgetViewModel.isStart) {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            Log.d("test", "onCreateSharedPref" + sharedPref.all.toString())
            Log.d("test", "onCreateViewModelBefore" + widgetViewModel.widgetList.toString())
            val sharedPrefSize = sharedPref.all.size / 3
            for (index in range(0, sharedPrefSize)) {
                val widget = sharedPref.getString(index.toString() + "widget", null)
                if (widget != null) {
                    widgetViewModel.widgetList.add(widget)
                }
                val settings = sharedPref.getString(index.toString() + "settings", null)
                if (settings != null) {
                    widgetViewModel.settingsList.add(settings)
                }
                val key = sharedPref.getString(index.toString() + "key", null)
                if (key != null) {
                    widgetViewModel.keyList.add(key)
                }
            }
            Log.d("test", "onCreateViewModelAfter" + widgetViewModel.widgetList.toString())
            widgetViewModel.isStart = false
        }
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_settings -> {
                findNavController().navigate(R.id.action_dashboardFragment2_to_appSettingsFragment)
                true
            }
            R.id.move_widget -> {
                if (!widgetViewModel.isMove) {
                    swipeHelper.attachToRecyclerView(null)
                    dragHelper.attachToRecyclerView(binding.recyclerView)
                    widgetViewModel.isMove = true
                    Toast.makeText(requireContext(), "Widget Movement Enabled", Toast.LENGTH_SHORT).show()
                } else {
                    swipeHelper.attachToRecyclerView(binding.recyclerView)
                    dragHelper.attachToRecyclerView(null)
                    widgetViewModel.isMove = false
                    Toast.makeText(requireContext(), "Widget Movement Disabled", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private var _binding: FragementDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragementDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        gridLayoutManager = GridLayoutManager(context,2)
        binding.recyclerView.layoutManager = gridLayoutManager
        recyclerGridAdapter = RecyclerGrid(widgetViewModel.widgetList, widgetViewModel.settingsList ,widgetViewModel.keyList, requireContext())
        binding.recyclerView.adapter = recyclerGridAdapter

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment2_to_addWidgetFragment)
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.app_settings -> {
                    findNavController().navigate(R.id.action_dashboardFragment2_to_appSettingsFragment)
                    true
                }
                R.id.move_widget -> {
                    if (!widgetViewModel.isMove) {
                        swipeHelper.attachToRecyclerView(null)
                        dragHelper.attachToRecyclerView(binding.recyclerView)
                        widgetViewModel.isMove = true
                        Toast.makeText(requireContext(), "Widget Movement Enabled", Toast.LENGTH_SHORT).show()
                    } else {
                        swipeHelper.attachToRecyclerView(binding.recyclerView)
                        dragHelper.attachToRecyclerView(null)
                        widgetViewModel.isMove = false
                        Toast.makeText(requireContext(), "Widget Movement Disabled", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }

        val swipedDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("test", "SwipeBefore" + widgetViewModel.widgetList.toString())
                if (direction == ItemTouchHelper.LEFT) {
                    recyclerGridAdapter.deleteItem(viewHolder.absoluteAdapterPosition)
                    Log.d("test", "SwipeAfter" + widgetViewModel.widgetList.toString())
                } else if (direction == ItemTouchHelper.RIGHT) {
                    val widgetSwiped = widgetViewModel.widgetList[viewHolder.absoluteAdapterPosition]

                    val action = DashboardFragmentDirections.actionDashboardFragment2ToWidgetSettingsFragment(viewHolder.absoluteAdapterPosition, widgetSwiped)
                    view.findNavController().navigate(action)

                }
            }
        }

        val dragWidgets = object : DragToMoveWidgets() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val adapter = binding.recyclerView.adapter as RecyclerGrid
                val from = viewHolder.absoluteAdapterPosition
                val to = target.absoluteAdapterPosition

                Collections.swap(widgetViewModel.widgetList, from, to)
                Collections.swap(widgetViewModel.settingsList, from, to)
                Collections.swap(widgetViewModel.keyList, from, to)

                adapter.notifyItemMoved(from, to)

                return true
            }
        }

        swipeHelper = ItemTouchHelper(swipedDelete)
        dragHelper = ItemTouchHelper(dragWidgets)

        swipeHelper.attachToRecyclerView(binding.recyclerView)

        return view
    }

    private fun addWidget(widget: String) {
        when (widget) {
            "Weather Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.weatherWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "Time Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.timeWidget)
                widgetViewModel.settingsList.add("timeSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "Chart Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.chartWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "News Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.newsWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "Finance Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.financeWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "MQTT Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.mqttWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())

            }
        }
    }

    private fun generateKey() : String {
        val keySize = 10

        var key = ""

        val alphaNumerics = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

        for (i in 1..keySize) {
            val index = (alphaNumerics.indices).random()

            key += alphaNumerics[index]
        }

        return key
    }

    private fun getWidgetSettings(key: String): String {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val preferences = sharedPrefs.all

        val keyValueOfPreference = preferences.filterKeys { it.contains(key) }

        var widgetData = ""
        for (keyValue in keyValueOfPreference) {
            val keyValueArray = keyValue.toString().split("=")
            val newKey = keyValueArray[0].split("_")[0]
            widgetData += newKey + "=" + keyValueArray[1] + ", "
        }

        return widgetData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
