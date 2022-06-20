package daniellopes.io.newsappstarter.ui.fragments.home

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
import daniellopes.io.newsappstarter.databinding.FragmentHomeBinding
import daniellopes.io.newsappstarter.repository.NewsRepository
import daniellopes.io.newsappstarter.ui.adapter.MainAdapter
import daniellopes.io.newsappstarter.ui.fragments.base.BaseFragment
import daniellopes.io.newsappstarter.util.hide
import daniellopes.io.newsappstarter.util.show
import daniellopes.io.newsappstarter.util.state.remote.StateResource
import daniellopes.io.newsappstarter.util.toast

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val mainAdapter by lazy { MainAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        observerResults()
    }

    private fun setupRecycleView() = with(binding) {
        rvNews.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }

        mainAdapter.setOnclickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToWebViewFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observerResults() {
        // precisamos obter um LifecycleOwner que represente o ciclo de vida do Fragmento
        //para receber um retorno de chamada para quando o ciclo de
        // vida da visualização do Fragment estiver disponível.
        viewModel.getAll.observe(viewLifecycleOwner, { response ->
            when (response) {
                is StateResource.Success -> {
                    binding.rvProgressBar.hide()
                    response.data?.let { data ->
                        mainAdapter.differ.submitList(data.articles.toList())
                    }
                }
                is StateResource.Error -> {
                    binding.rvProgressBar.hide()
                    toast("Um erro ocorreu: ${response.message.toString()}")
                }
                is StateResource.Loading -> {
                    binding.rvProgressBar.show()
                }
            }
        })
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): NewsRepository =
        NewsRepository(RetrofitInstance.api, ArticleDatabase.invoke(requireContext()))
}