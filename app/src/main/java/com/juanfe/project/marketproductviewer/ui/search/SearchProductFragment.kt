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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchView.TransitionState
import com.juanfe.project.marketproductviewer.R
import com.juanfe.project.marketproductviewer.databinding.FragmentSearchProductBinding
import com.juanfe.project.marketproductviewer.ui.search.adapter.product.ProductAdapter
import com.juanfe.project.marketproductviewer.ui.search.adapter.search.SearchHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductFragment : Fragment() {

    private var _binding: FragmentSearchProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchProductViewModel.searchHistory.collect { allHistory ->
                    Log.e("History", allHistory.toString())
                    if (allHistory != null)
                        searchHistoryAdapter.updateList(allHistory)
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
                if (viewState.firstOpen) {
                    binding.progress.visibility = View.GONE
                    binding.msgInformation.text =
                        requireContext().getString(R.string.search_something)
                } else {
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
        // Rv - products from the api

        binding.productRv.layoutManager = LinearLayoutManager(requireContext())

        productAdapter = ProductAdapter(listOf()) {
            val action =
                SearchProductFragmentDirections.actionSearchProductFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }
        binding.productRv.adapter = productAdapter


        // Rv - history products from the dataStore
        binding.historyRv.layoutManager = LinearLayoutManager(requireContext())

        searchHistoryAdapter = SearchHistoryAdapter(listOf()) { query, search ->
            if (search) {
                searchProductViewModel.handleIntent(UserIntent.SearchProduct(query))
                binding.searchBar.setText(query)
                binding.searchView.hide()
            } else {
                binding.searchView.setText(query)
                binding.searchBar.setText(query)
            }
        }
        binding.historyRv.adapter = searchHistoryAdapter

    }

    private fun initListeners() {
        binding.apply {

            searchView.addTransitionListener { _, transitionState, _ ->
                if (transitionState == TransitionState.SHOWING) {
                    searchProductViewModel.handleIntent(UserIntent.TapSearch)
                }
            }

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


