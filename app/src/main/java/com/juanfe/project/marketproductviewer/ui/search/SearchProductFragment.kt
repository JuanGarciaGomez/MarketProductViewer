package com.juanfe.project.marketproductviewer.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanfe.project.marketproductviewer.databinding.FragmentSearchProductBinding
import com.juanfe.project.marketproductviewer.ui.search.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductFragment : Fragment() {

    private var _binding: FragmentSearchProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    private val searchProductViewModel: SearchProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
        setUpRecyclerView()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchProductViewModel.viewState.collect { viewState ->
                    updateUi(viewState)
                }
            }
        }
    }

    private fun updateUi(viewState: SearchProductViewState) {
        when (viewState) {
            is SearchProductViewState.Error -> {
                productAdapter.updateList(listOf())
                binding.msgInformation.visibility = View.VISIBLE
                binding.msgInformation.text = viewState.errorMsg
                hideLoading(visibleRv = false)
            }

            is SearchProductViewState.Loading -> {
                if (viewState.firstOpen) binding.progress.visibility = View.GONE
                else {
                    productAdapter.updateList(listOf())
                    binding.msgInformation.visibility = View.GONE
                    binding.progress.visibility = View.VISIBLE
                }
            }

            is SearchProductViewState.Success -> {
                productAdapter.updateList(viewState.resultModel)
                hideLoading(visibleRv = true)
            }
        }
    }

    private fun hideLoading(visibleRv: Boolean, visibleProgress: Boolean = false) {
        binding.productRv.isVisible = visibleRv
        binding.progress.isVisible = visibleProgress
    }

    private fun setUpRecyclerView() {
        binding.productRv.layoutManager = LinearLayoutManager(requireContext())

        //I can save de first search for when the user re open the app he have the last search
        productAdapter = ProductAdapter(listOf()) {
            // findNavController().navigate(navigate)

        }
        binding.productRv.adapter = productAdapter
    }

    private fun initListeners() {
        binding.apply {
            searchView.editText.setOnEditorActionListener { query, _, _ ->
                val text = query?.text.toString()
                searchBar.setText(text)
                searchView.hide()
                if (text.isNotEmpty())
                    searchProductViewModel.handleIntent(UserIntent.SearchProduct(text))
                true
            }
        }
    }

}


