package daniellopes.io.newsappstarter.ui.fragments.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import daniellopes.io.newsappstarter.R
import daniellopes.io.newsappstarter.data.local.db.ArticleDatabase
import daniellopes.io.newsappstarter.data.remote.RetrofitInstance
import daniellopes.io.newsappstarter.databinding.FragmentFavoritesBinding
import daniellopes.io.newsappstarter.repository.NewsRepository
import daniellopes.io.newsappstarter.ui.adapter.MainAdapter
import daniellopes.io.newsappstarter.ui.fragments.base.BaseFragment
import daniellopes.io.newsappstarter.util.state.local.ArticleListEvent
import daniellopes.io.newsappstarter.util.state.local.ArticleListState

class FavoriteFragment : BaseFragment<FavoriteViewModel, FragmentFavoritesBinding>() {

    private val mainAdapter by lazy { MainAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dispatch(ArticleListEvent.Fetch)
        setupRecycleView()
        observerResult()
    }

    private fun observerResult() {
        viewModel.favorite.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ArticleListState.Success -> {
                    binding.textView.visibility = View.INVISIBLE
                    mainAdapter.differ.submitList(state.list)
                }
                is ArticleListState.ErrorMessage -> {
                    binding.textView.visibility = View.INVISIBLE
                    Toast.makeText(
                        context,
                        "Um erro ocorreu: ${state.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ArticleListState.Loading -> {
                    binding.textView.visibility = View.INVISIBLE
                }
                ArticleListState.Empty -> {
                    binding.textView.visibility = View.VISIBLE
                    mainAdapter.differ.submitList(emptyList())
                }
            }
        })
    }

    private fun setupRecycleView() = with(binding) {
        rvFavorite.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }

        mainAdapter.setOnclickListener {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToWebViewFragment(it)
            findNavController().navigate(action)
        }
    }


    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val article = mainAdapter.differ.currentList[position]
            viewModel.deleteArticle(article)
            Snackbar.make(
                viewHolder.itemView,
                getString(R.string.article_delete_successful),
                Snackbar.LENGTH_LONG
            ).apply {
                setAction(getString(R.string.undo)) {
                    viewModel.saveArticle(article)
                    mainAdapter.notifyItemInserted(position)
                }
                show()
            }
            observerResult()
        }
    }

    override fun getViewModel(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): NewsRepository =
        NewsRepository(RetrofitInstance.api, ArticleDatabase.invoke(requireContext()))
}