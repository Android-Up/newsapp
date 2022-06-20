package daniellopes.io.newsappstarter.presenter.search

import daniellopes.io.newsappstarter.data.local.model.NewsResponse
import daniellopes.io.newsappstarter.repository.NewsDataSource
import daniellopes.io.newsappstarter.presenter.ViewHome

class SearchPresenter(
    val view: ViewHome.View,
    private val dataSource: NewsDataSource
) : SearchHome.Presenter {

    override fun search(term: String) {
        this.view.showProgressBar()
        this.dataSource.searchNews(term, this)
    }

    override fun onSuccess(newsResponse: NewsResponse) {
        this.view.showArticles(newsResponse.articles)
    }

    override fun onError(message: String) {
        this.view.showFailure(message)
    }

    override fun onComplete() {
        this.view.hideProgressBar()
    }
}