package com.juanfe.project.marketproductviewer.ui.detail

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.core.ex.SpanTarget
import com.juanfe.project.marketproductviewer.core.ex.formatToCOP
import com.juanfe.project.marketproductviewer.core.ex.loadProductImg
import com.juanfe.project.marketproductviewer.core.ex.span
import com.juanfe.project.marketproductviewer.databinding.FragmentDetailBinding
import com.juanfe.project.marketproductviewer.domain.ResultModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var product: ResultModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initListeners()
        drawProduct()
    }

    private fun initListeners() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.icShare.setOnClickListener {
            shareProduct()
        }

    }

    private fun drawProduct() {
        product = args.product
        binding.apply {

            prodName.text = product.title
            prodImg.loadProductImg(product.thumbnail)

            validatePrice()
            handleInstallments()
            validateShipping()
        }
    }

    private fun shareProduct() {
        val url = product.permalink
        val shareMessage = "¡Echa un vistazo a este producto: $url"

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }
        startActivity(intent)
    }


    private fun validateShipping() {
        binding.apply {
            if (product.shipping.freeShipping) {
                prodDelivery.text =
                    getString(R.string.free_delivery).span(
                        requireContext(),
                        getString(R.string.all_country), SpanTarget.MAIN_TEXT
                    )
            } else {
                prodDelivery.text = getString(R.string.delivery)
            }
        }
    }

    private fun validatePrice() {
        binding.apply {
            if (product.originalPrice.formatToCOP() != product.price.formatToCOP()) {
                prodOriginalPrice.visibility = View.VISIBLE
                prodOriginalPrice.text = product.originalPrice.formatToCOP()
                prodOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                productPrice.text = product.price.formatToCOP()
                    .span(requireContext(), "for SALE", SpanTarget.SELECTED_PART)
            } else {
                prodOriginalPrice.visibility = View.GONE
                productPrice.text = product.price.formatToCOP()
            }
        }

    }

    private fun handleInstallments() {
        val installments = product.installments
        if (installments != null) {
            val msgInstallments = "en"
            binding.prodInstallments.text = msgInstallments.span(
                requireContext(),
                "${installments.quantity} cuotas de ${installments.amount.formatToCOP()} con ${installments.rate}% interés",
                SpanTarget.SELECTED_PART
            )
        } else binding.prodInstallments.visibility = View.GONE
    }


}