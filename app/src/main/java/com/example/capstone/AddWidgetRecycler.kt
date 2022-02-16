package com.example.capstone

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController

class AddWidgetRecycler(private val dataSet: Array<String>, private val dataIconSet: Array<Int>) :
    RecyclerView.Adapter<AddWidgetRecycler.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.add_widget_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]
        viewHolder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(dataIconSet[position], 0,0 ,0)

        val widgetClicked = viewHolder.textView.text

        val action = AddWidgetFragmentDirections.actionAddWidgetFragmentToWidgetSettingsFragment(widgetClicked.toString())

        // Define click listener for the textView.
        viewHolder.textView.setOnClickListener {
            viewHolder.textView.findNavController()
                .navigate(action)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
