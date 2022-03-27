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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.io.*
import java.util.*
import java.util.stream.IntStream.range

class DashboardFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var  recyclerGridAdapter: RecyclerGrid
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
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.core_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_settings -> {
                findNavController().navigate(R.id.action_dashboardFragment2_to_appSettingsFragment)
                true
            }
            R.id.add_widget -> {
                findNavController().navigate(R.id.action_dashboardFragment2_to_addWidgetFragment)
                true
            }
            R.id.move_widget -> {
                if (!widgetViewModel.isMove) {
                    swipeHelper.attachToRecyclerView(null)
                    dragHelper.attachToRecyclerView(recycler)
                    widgetViewModel.isMove = true
                    Toast.makeText(requireContext(), "Widget Movement Enabled", Toast.LENGTH_SHORT).show()
                } else {
                    swipeHelper.attachToRecyclerView(recycler)
                    dragHelper.attachToRecyclerView(null)
                    widgetViewModel.isMove = false
                    Toast.makeText(requireContext(), "Widget Movement Disabled", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragement_dashboard, container, false)

        recycler = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = GridLayoutManager(context,2)
        recyclerGridAdapter = RecyclerGrid(widgetViewModel.widgetList, widgetViewModel.settingsList, widgetViewModel.keyList, requireContext())
        recycler.adapter = recyclerGridAdapter



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
                val adapter = recycler.adapter as RecyclerGrid
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

        swipeHelper.attachToRecyclerView(recycler)

        return view
    }

    private fun addWidget(widget: String) {
        val key = generateKey()

        when (widget) {
            "Weather Widget" -> {
                requireContext().assets.open(widgetViewModel.weatherWidget).read()
                val splitFileExtension = widgetViewModel.weatherWidget.split(".")

                createAndWriteToWidgetDir( requireContext(),splitFileExtension[0] + key + "." + splitFileExtension[1], readAssetFile(widgetViewModel.weatherWidget))

                widgetViewModel.widgetList.add(widgetViewModel.path + splitFileExtension[0] + key + "." + splitFileExtension[1])
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(key)
            }
            "Time Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.path + widgetViewModel.timeWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "Chart Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.path + widgetViewModel.chartWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "News Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.path + widgetViewModel.newsWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "Finance Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.path + widgetViewModel.financeWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())
            }
            "MQTT Widget" -> {
                widgetViewModel.widgetList.add(widgetViewModel.path + widgetViewModel.mqttWidget)
                widgetViewModel.settingsList.add("weatherSettings.json")
                widgetViewModel.keyList.add(generateKey())

            }
        }
    }

    private fun generateKey() : String {
        val alphaNumerics = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        var key = ""
        val keySize = 10
        while (widgetViewModel.keyList.contains(key) || key == "") {
            for (i in 1..keySize) {
                val index = (alphaNumerics.indices).random()

                key += alphaNumerics[index]
            }
        }

        return key
    }

    //https://stackoverflow.com/questions/44587187/android-how-to-write-a-file-to-internal-storage
    private fun createAndWriteToWidgetDir(context: Context, fileName: String, widgetCode: String) {
        val widgetDir = File(context.filesDir, "widget")
        if (!widgetDir.exists()) {
            widgetDir.mkdir()
        }

        try {
            val widgetFile = File(widgetDir, fileName)
            val writer = FileWriter(widgetFile)
            writer.append(widgetCode)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // https://stackoverflow.com/questions/9544737/read-file-from-assets
    private fun readAssetFile(fileName: String): String {
        return requireContext().assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
