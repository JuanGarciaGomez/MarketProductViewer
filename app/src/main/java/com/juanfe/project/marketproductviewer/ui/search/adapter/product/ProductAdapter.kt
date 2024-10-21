package com.juanfe.project.marketproductviewer.ui.search.adapter.product

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.core.ex.formatToCOP
import com.juanfe.project.marketproductviewer.core.ex.loadProductImg
import com.juanfe.project.marketproductviewer.databinding.ItemProductBinding
import com.juanfe.project.marketproductviewer.domain.ResultModel


class ProductAdapter(
    private var list: List<ResultModel>,
    private val onItemSelected: (ResultModel) -> Unit
) : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    fun updateList(newList: List<ResultModel>) {
        val allOrderDiff = ProductDiffUtil(list, newList)
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
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class MyViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultModel, onItemSelected: (ResultModel) -> Unit) {
            binding.apply {
                productTitle.text = item.title

                if (item.originalPrice.formatToCOP() != item.price.formatToCOP()) {
                    productOriginalPrice.visibility = View.VISIBLE
                    productOriginalPrice.text = item.originalPrice.formatToCOP()
                    productOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    productOriginalPrice.visibility = View.GONE
                }
                productPrice.text = item.price.formatToCOP()

                if (item.shipping.freeShipping) {
                    productShipping.text = root.context.getString(R.string.free_delivery)
                } else {
                    productShipping.visibility = View.GONE
                }

                productImg.loadProductImg(item.thumbnail)

                product.setOnClickListener {
                    onItemSelected.invoke(item)
                }
            }
        }

    }

}