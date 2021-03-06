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
            val sharedPrefSize = sharedPref.all.size / 3
            for (index in range(0, sharedPrefSize)) {
                val widget = sharedPref.getString(index.toString() + "widget", null)
                val settings = sharedPref.getString(index.toString() + "settings", null)
                val key = sharedPref.getString(index.toString() + "key", null)
                if (widget != null && settings != null && key != null) {
                    widgetViewModel.widgetList.add(Widget(widget, settings, key))
                }
            }
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
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.movement_enabled),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    swipeHelper.attachToRecyclerView(binding.recyclerView)
                    dragHelper.attachToRecyclerView(null)
                    widgetViewModel.isMove = false
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.movement_disabled),
                        Toast.LENGTH_SHORT
                    ).show()
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
        recyclerGridAdapter = RecyclerGrid(widgetViewModel.widgetList, requireContext())
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
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.movement_enabled),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        swipeHelper.attachToRecyclerView(binding.recyclerView)
                        dragHelper.attachToRecyclerView(null)
                        widgetViewModel.isMove = false
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.movement_disabled),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    true
                }
                else -> false
            }
        }

        val swipedDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    recyclerGridAdapter.deleteItem(viewHolder.absoluteAdapterPosition)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    val widgetSwiped = widgetViewModel.widgetList[viewHolder.absoluteAdapterPosition]

                    val action = DashboardFragmentDirections.actionDashboardFragment2ToWidgetSettingsFragment(viewHolder.absoluteAdapterPosition, widgetSwiped.widgetUrl)
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
            getString(R.string.weather_widget) -> {
                widgetViewModel.widgetList.add(Widget(widgetViewModel.weatherWidget,
                    "weatherSettings.json",
                    generateKey()))
            }
            getString(R.string.time_widget) -> {
                widgetViewModel.widgetList.add(Widget(widgetViewModel.timeWidget,
                    "timeSettings.json",
                    generateKey()))
            }
            getString(
                R.string.reddit_widget),
            -> {
                widgetViewModel.widgetList.add(Widget(widgetViewModel.redditWidget,
                    "redditSettings.json",
                    generateKey()))
            }
            getString(R.string.stocks_widget) -> {
                widgetViewModel.widgetList.add(Widget(widgetViewModel.stocksWidget,
                    "stocksSettings.json",
                    generateKey()))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
