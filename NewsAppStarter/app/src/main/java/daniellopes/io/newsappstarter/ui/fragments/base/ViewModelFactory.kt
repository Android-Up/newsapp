package daniellopes.io.newsappstarter.ui.fragments.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import daniellopes.io.newsappstarter.repository.NewsRepository
import daniellopes.io.newsappstarter.ui.fragments.favorite.FavoriteViewModel
import daniellopes.io.newsappstarter.ui.fragments.home.HomeViewModel
import daniellopes.io.newsappstarter.ui.fragments.search.SearchViewModel
import daniellopes.io.newsappstarter.ui.fragments.webview.WebViewViewModel

// Forma de instanciarmos os viewModels, passando o que eu quero para ele, no caso, o repositorio que recebemos do BaseFragment
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: NewsRepository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {
//responsáveis por instanciar ViewModels .

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository, application) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository) as T
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(repository) as T
            modelClass.isAssignableFrom(WebViewViewModel::class.java) -> WebViewViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}