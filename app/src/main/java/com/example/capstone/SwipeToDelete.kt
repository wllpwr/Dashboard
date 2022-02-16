package com.example.capstone

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeToDelete(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ) {

    val deleteColor = ContextCompat.getColor(context, R.color.red)
    val widgetSettingsColor = ContextCompat.getColor(context, R.color.light_gray)
    val deleteIcon = R.drawable.ic_baseline_delete_sweep_24
    val widgetSettingsIcon = R.drawable.ic_baseline_settings_24

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {


        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightBackgroundColor(widgetSettingsColor)
            .addSwipeRightActionIcon(widgetSettingsIcon)
            .create()
            .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX / 4, dY, actionState, isCurrentlyActive)
    }
}