package daniellopes.io.newsappstarter.presenter.search

import daniellopes.io.newsappstarter.data.local.model.NewsResponse

interface SearchHome {

    interface Presenter{
        fun search(term: String)
        fun onSuccess(newsResponse: NewsResponse)
        fun onError(message: String)
        fun onComplete()
    }
}