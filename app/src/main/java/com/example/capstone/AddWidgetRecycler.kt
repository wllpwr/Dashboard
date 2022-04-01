package com.example.capstone

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.capstone.databinding.AddWidgetListItemBinding

class AddWidgetRecycler(private val dataSet: Array<String>, private val dataIconSet: Array<Int>) :
    RecyclerView.Adapter<AddWidgetRecycler.ViewHolder>() {

    inner class ViewHolder(val binding: AddWidgetListItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddWidgetListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            with(dataSet[position]) {
                binding.textView.text = this

                with(dataIconSet[position]) {
                    binding.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(this, 0,0 ,0)
                }

                val widgetClicked = binding.textView.text

                val action = AddWidgetFragmentDirections.actionAddWidgetFragmentToDashboardFragment2(widgetClicked.toString())

                // Define click listener for the textView.
                binding.textView.setOnClickListener {
                    binding.textView.findNavController()
                        .navigate(action)
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
