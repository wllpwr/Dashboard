package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private lateinit var  recyclerGridAdapter: RecyclerGrid
    private val widgetViewModel: WidgetViewModel by viewModels()
    private var dataList = ArrayList<String>()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.core_menu, menu)
        return true
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.widget_menu, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        recyclerGridAdapter = RecyclerGrid()
        recyclerView.adapter = recyclerGridAdapter

        val swipedDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("testing", "test")
                recyclerGridAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }

        val swipeHelper = ItemTouchHelper(swipedDelete)
        swipeHelper.attachToRecyclerView(recyclerView)

        val dragWidgets = object : DragToMoveWidgets() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Log.d("testing", "test2")
                val adapter = recyclerView.adapter as RecyclerGrid
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                adapter.moveItem(from, to)

                adapter.notifyItemMoved(from, to)

                return true
            }
        }

        val dragHelper = ItemTouchHelper(dragWidgets)
        dragHelper.attachToRecyclerView(recyclerView)

        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget2)
        dataList.add(widgetViewModel.encodedWidget3)
        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget1)

        recyclerGridAdapter.setDataList(dataList)
    }
}