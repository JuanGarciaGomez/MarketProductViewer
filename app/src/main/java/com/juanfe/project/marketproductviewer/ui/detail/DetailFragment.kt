package com.juanfe.project.marketproductviewer.ui.detail

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.core.ex.SpanTarget
import com.juanfe.project.marketproductviewer.core.ex.formatToCOP
import com.juanfe.project.marketproductviewer.core.ex.span
import com.juanfe.project.marketproductviewer.databinding.FragmentDetailBinding
import com.juanfe.project.marketproductviewer.domain.ResultModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var carouselDetailAdapter: CarouselDetailAdapter


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
        detailViewModel.setProduct(args.product)
        initUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    private fun initUi() {
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.viewState.collect { viewState ->
                    updateUi(viewState)
                }
            }
        }
    }

    private fun updateUi(viewState: DetailViewState) {
        when (viewState) {
            is DetailViewState.Success -> {
                if (viewState.resultModel != null) {
                    product = viewState.resultModel
                    drawProduct()
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.share -> {
                        shareProduct()
                        true
                    }

                    else -> false
                }
            }

            btnBuy.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.soon), Toast.LENGTH_SHORT).show()
            }

            btnCar.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.very_soon), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun drawProduct() {
        binding.apply {
            setUpRecyclerView(product.thumbnail)
            prodName.text = product.title
            validatePrice()
            handleInstallments()
            validateShipping()
        }
    }

    private fun setUpRecyclerView(thumbnail: String) {
        val layoutManager =
            object : CarouselLayoutManager(HeroCarouselStrategy()) {
                override fun canScrollVertically(): Boolean = false
            }.apply {
                carouselAlignment = CarouselLayoutManager.ALIGNMENT_CENTER
            }
        binding.carouselRecyclerView.layoutManager = layoutManager

        carouselDetailAdapter = CarouselDetailAdapter(listOf(thumbnail, thumbnail))
        binding.carouselRecyclerView.adapter = carouselDetailAdapter

        // Checks if an OnFlingListener is already assigned and if it is of type CarouselSnapHelper
        if (binding.carouselRecyclerView.onFlingListener !is CarouselSnapHelper) {
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
        }
    }


    private fun shareProduct() {
        val url = product.permalink
        val shareMessage = getString(R.string.look_product) + url

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