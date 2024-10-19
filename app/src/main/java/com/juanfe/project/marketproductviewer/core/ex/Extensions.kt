package com.juanfe.project.marketproductviewer.core.ex

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.juanfe.project.marketproductviewer.R
import java.text.NumberFormat
import java.util.Locale

fun ImageView.loadProductImg(img: String) {
    val url = img.replace("http:", "https:").replace("-I", "-O")
    pretty(url).into(this)
}

fun View.pretty(url: String): RequestBuilder<Drawable> {
    return Glide
        .with(this)
        .load(url)
        .fitCenter()
        .placeholder(R.mipmap.ic_launcher_round)
}

fun Double.formatToCOP(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    format.maximumFractionDigits = 0
    return format.format(this)
}