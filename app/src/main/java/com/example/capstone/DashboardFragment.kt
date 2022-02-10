package com.example.capstone

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DashboardFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var  recyclerGridAdapter: RecyclerGrid
    private val widgetViewModel: WidgetViewModel by viewModels()
    private var dataList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        recyclerGridAdapter = RecyclerGrid()
        recycler.adapter = recyclerGridAdapter

        val swipedDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                recyclerGridAdapter.deleteItem(viewHolder.adapterPosition)
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

                adapter.moveItem(from, to)

                adapter.notifyItemMoved(from, to)

                return true
            }
        }

        val dragHelper = ItemTouchHelper(dragWidgets)
        dragHelper.attachToRecyclerView(recycler)

        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget2)
        dataList.add(widgetViewModel.encodedWidget3)
        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget1)

        recyclerGridAdapter.setDataList(dataList)

        return view
    }
}
