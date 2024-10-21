package com.juanfe.project.marketproductviewer.core.ex

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
        .sizeMultiplier(0.5f)
        .placeholder(R.mipmap.ic_launcher_round)
}

fun Double.formatToCOP(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    format.maximumFractionDigits = 0
    return format.format(this)
}

enum class SpanTarget {
    MAIN_TEXT,
    SELECTED_PART
}

fun String.span(
    context: Context,
    selectedPart: String,
    target: SpanTarget
): SpannableStringBuilder {
    val completedText = SpannableStringBuilder("$this $selectedPart")

    when (target) {
        SpanTarget.MAIN_TEXT -> {
            completedText.apply {
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    this@span.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.green)),
                    0,
                    this@span.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        SpanTarget.SELECTED_PART -> {
            completedText.apply {
                val start = this@span.length + 1
                val end = start + selectedPart.length
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.green)),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
    return completedText
}
