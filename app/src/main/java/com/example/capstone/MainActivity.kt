package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var  recyclerGridAdapter: RecyclerGrid
    private val widgetViewModel: WidgetViewModel by viewModels()
    private var dataList = mutableListOf<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        recyclerGridAdapter = RecyclerGrid(applicationContext)
        recyclerView.adapter = recyclerGridAdapter

        dataList.add(DataModel(widgetViewModel.encodedWidget1))
        dataList.add(DataModel(widgetViewModel.encodedWidget2))
        dataList.add(DataModel(widgetViewModel.encodedWidget2))
        dataList.add(DataModel(widgetViewModel.encodedWidget2))
        dataList.add(DataModel(widgetViewModel.encodedWidget2))
        dataList.add(DataModel(widgetViewModel.encodedWidget2))

        recyclerGridAdapter.setDataList(dataList)
    }
}