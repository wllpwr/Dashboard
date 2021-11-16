package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private lateinit var  recyclerGridAdapter: RecyclerGrid
    private val widgetViewModel: WidgetViewModel by viewModels()
    private var dataList = mutableListOf<String>()

    // https://yfujiki.medium.com/drag-and-reorder-recyclerview-items-in-a-user-friendly-manner-1282335141e9
    // https://github.com/yfujiki/Android-DragReorderSample/blob/master/app/src/main/java/com/yfujiki/android_dragreordersample/MainActivity.kt
    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or
                        ItemTouchHelper.DOWN or
                        ItemTouchHelper.START or
                        ItemTouchHelper.END, 0) {

                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {

                    val adapter = recyclerView.adapter as RecyclerGrid
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    adapter.moveItem(from, to)

                    adapter.notifyItemMoved(from, to)

                    return true
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                      direction: Int) {
                }

                override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                    super.onSelectedChanged(viewHolder, actionState)

                    if (actionState == ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.alpha = 0.5f
                    }
                }

                override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                    super.clearView(recyclerView, viewHolder)

                    viewHolder.itemView.alpha = 1.0f
                }
            }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        recyclerGridAdapter = RecyclerGrid(applicationContext)
        recyclerView.adapter = recyclerGridAdapter
        itemTouchHelper.attachToRecyclerView(recyclerView)

        dataList.add(widgetViewModel.encodedWidget1)
        dataList.add(widgetViewModel.encodedWidget2)
        dataList.add(widgetViewModel.encodedWidget2)
        dataList.add(widgetViewModel.encodedWidget2)
        dataList.add(widgetViewModel.encodedWidget2)
        dataList.add(widgetViewModel.encodedWidget2)

        recyclerGridAdapter.setDataList(dataList)
    }
}