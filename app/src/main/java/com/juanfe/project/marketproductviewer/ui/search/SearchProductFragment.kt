package com.juanfe.project.marketproductviewer.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                binding.productRv.visibility = View.GONE
                binding.msgInformation.visibility = View.VISIBLE
                binding.msgInformation.text = viewState.errorMsg
            }

            SearchProductViewState.Loading -> {
                //handleLoading
            }

            is SearchProductViewState.Success -> {
                binding.productRv.visibility = View.VISIBLE
                productAdapter.updateList(viewState.resultModel)
                binding.msgInformation.visibility = View.GONE
            }
        }
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
            searchView.editText.setOnEditorActionListener { query, actionId, event ->
                val text = query?.text.toString()
                searchBar.setText(text);
                searchView.hide()
                searchProductViewModel.handleIntent(UserIntent.SearchProduct(text))
                true
            }
        }
    }

}


