package daniellopes.io.newsappstarter.data.local.model

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)