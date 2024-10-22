package com.juanfe.project.marketproductviewer.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juanfe.project.marketproductviewer.core.ex.loadProductImg
import com.juanfe.project.marketproductviewer.databinding.CarouselItemBinding
import javax.inject.Inject


class CarouselDetailAdapter @Inject constructor(
    private var list: List<String>
) : RecyclerView.Adapter<CarouselDetailAdapter.CarouselHomeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarouselHomeViewHolder {
        val binding =
            CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselHomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselHomeViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            carouselImageView.loadProductImg(item)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class CarouselHomeViewHolder(val binding: CarouselItemBinding) : RecyclerView.ViewHolder(binding.root)


}