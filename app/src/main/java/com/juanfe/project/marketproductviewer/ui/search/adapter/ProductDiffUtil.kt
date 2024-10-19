package com.juanfe.project.marketproductviewer.ui.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.juanfe.project.marketproductviewer.domain.ResultModel


class ProductDiffUtil(
    private val oldList: List<ResultModel>,
    private val newList: List<ResultModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // I can use whatever validations that i need
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition] == newList[newItemPosition]
    }


}