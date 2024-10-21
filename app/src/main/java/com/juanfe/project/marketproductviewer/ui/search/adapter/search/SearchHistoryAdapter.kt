package com.juanfe.project.marketproductviewer.ui.search.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juanfe.project.marketproductviewer.databinding.ItemProductHistoryBinding


class SearchHistoryAdapter(
    private var list: List<String>,
    private val onItemSelected: (String, Boolean) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder>() {

    fun updateList(newList: List<String>) {
        val allOrderDiff = SearchHistoryDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(allOrderDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }


    /**
     * This method is called when the RecyclerView needs a new view to represent an item.
     * This is where the layout of the RecyclerView items is inflated.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemProductHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = list.size


    /**
     * This method is called to assign data to the inflated views.
     * Uses the provided ViewHolder to set up the item view at the specific position.
     */

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, onItemSelected)
    }

    class MyViewHolder(private val binding: ItemProductHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, onItemSelected: (String, Boolean) -> Unit) {
            binding.apply {

                historyProduct.text = item

                historyArrow.setOnClickListener {
                    onItemSelected.invoke(item, false)
                }

                historySchedule.setOnClickListener {
                    onItemSelected.invoke(item, true)
                }

                historyProduct.setOnClickListener {
                    onItemSelected.invoke(item, true)
                }

            }
        }

    }

}