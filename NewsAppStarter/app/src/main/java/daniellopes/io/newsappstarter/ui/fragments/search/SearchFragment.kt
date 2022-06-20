package daniellopes.io.newsappstarter.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import daniellopes.io.newsappstarter.R
import daniellopes.io.newsappstarter.data.local.db.ArticleDatabase
import daniellopes.io.newsappstarter.data.remote.RetrofitInstance
import daniellopes.io.newsappstarter.databinding.FragmentSearchBinding
import daniellopes.io.newsappstarter.repository.NewsRepository
import daniellopes.io.newsappstarter.ui.adapter.MainAdapter
import daniellopes.io.newsappstarter.ui.fragments.base.BaseFragment
import daniellopes.io.newsappstarter.ui.fragments.home.HomeFragmentDirections
import daniellopes.io.newsappstarter.util.state.remote.StateResource
import daniellopes.io.newsappstarter.util.UtilQueryTextListener

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    private val mainAdapter by lazy { MainAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        search()
        observerResults()
    }

    private fun observerResults() {
        viewModel.search.observe(viewLifecycleOwner, { response ->
            when(response){
                is StateResource.Success -> {
                    binding.rvProgressBarSearch.visibility = View.INVISIBLE
                    response.data?.let { data ->
                        mainAdapter.differ.submitList(data.articles.toList())
                    }
                }
                is StateResource.Error -> {
                    binding.rvProgressBarSearch.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Um erro ocorreu: ${response.message.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is StateResource.Loading -> {
                    binding.rvProgressBarSearch.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupRecycleView() {
        with(binding.rvSearch) {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }

        mainAdapter.setOnclickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToWebViewFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun search() {
        binding.searchNews.setOnQueryTextListener(
            UtilQueryTextListener(
                this.lifecycle
            ) { newText ->
                newText?.let { query ->
                    if (query.isNotEmpty()) {
                        viewModel.fetchSearch(query)
                        binding.rvProgressBarSearch.visibility = View.VISIBLE
                    }
                }
            }
        )
    }

    override fun getViewModel() = SearchViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): NewsRepository =
        NewsRepository(RetrofitInstance.api, ArticleDatabase.invoke(requireContext()))
}