package com.example.capstone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.stream.IntStream.range

class DashboardFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var  recyclerGridAdapter: RecyclerGrid
    private val widgetViewModel: WidgetViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //https://stackoverflow.com/questions/17401799/how-to-know-how-many-shared-preference-in-shared-preferences-in-android/17401994
        if (widgetViewModel.isStart) {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            Log.d("test", "onCreateSharedPref" + sharedPref.all.toString())
            Log.d("test", "onCreateViewModelBefore" + widgetViewModel.widgetList.toString())
            val sharedPrefSize = sharedPref.all.size
            for (index in range(0, sharedPrefSize)) {
                val widget = sharedPref.getString(index.toString(), null)
                if (widget != null) {
                    widgetViewModel.widgetList.add(widget)
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
        recyclerGridAdapter = RecyclerGrid(widgetViewModel.widgetList)
        recycler.adapter = recyclerGridAdapter

        val swipedDelete = object : SwipeToDelete(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("test", "SwipeBefore" + widgetViewModel.widgetList.toString())
                if (direction == ItemTouchHelper.LEFT) {
                    recyclerGridAdapter.deleteItem(viewHolder.adapterPosition)
                    Log.d("test", "SwipeAfter" + widgetViewModel.widgetList.toString())
                } else if (direction == ItemTouchHelper.RIGHT) {
                    val widgetSwiped = widgetViewModel.widgetList[viewHolder.adapterPosition]

                    val action = DashboardFragmentDirections.actionDashboardFragment2ToWidgetSettingsFragment("*$widgetSwiped")
                    view.findNavController().navigate(action)

                }
            }
        }

        val swipeHelper = ItemTouchHelper(swipedDelete)
        swipeHelper.attachToRecyclerView(recycler)

        val dragWidgets = object : DragToMoveWidgets() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val adapter = recycler.adapter as RecyclerGrid
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                Collections.swap(widgetViewModel.widgetList, from, to)

                adapter.notifyItemMoved(from, to)

                return true
            }
        }

        val dragHelper = ItemTouchHelper(dragWidgets)
        dragHelper.attachToRecyclerView(recycler)

        return view
    }
}
