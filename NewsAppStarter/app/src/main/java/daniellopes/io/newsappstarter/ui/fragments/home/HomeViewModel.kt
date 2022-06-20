package daniellopes.io.newsappstarter.ui.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import daniellopes.io.newsappstarter.data.local.model.NewsResponse
import daniellopes.io.newsappstarter.repository.NewsRepository
import daniellopes.io.newsappstarter.util.checkForInternetConnection
import daniellopes.io.newsappstarter.util.state.remote.StateResource
import kotlinx.coroutines.launch
import retrofit2.Response
// não liberar recursos de RAM quando não forem mais necessários.
class HomeViewModel constructor(
    private val repository: NewsRepository,
    context: Application
) : AndroidViewModel(context) {

    //nomes para propriedades de apoio

    private val _getAll = MutableLiveData<StateResource<NewsResponse>>()
    val getAll: LiveData<StateResource<NewsResponse>> = _getAll

    init {
        fetchAll()
    }

    private fun fetchAll() = viewModelScope.launch {
        safeFetchAll()
    }

    private suspend fun safeFetchAll() {
        _getAll.value = StateResource.Loading()
        try {
            if (checkForInternetConnection(getApplication())) {
                val response = repository.getAllRemote()
                _getAll.value = handleResponse(response)
            } else {
                _getAll.value = StateResource.Error("Sem conexão com a internet")
            }
        } catch (e: Exception) {
            _getAll.value = StateResource.Error("Artigos não encontrados: ${e.message}")
        }
    }

    private fun handleResponse(response: Response<NewsResponse>): StateResource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return StateResource.Success(values)
            }
        }
        return StateResource.Error(response.message())
    }
}