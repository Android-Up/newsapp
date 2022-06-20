package daniellopes.io.newsappstarter.util.state.remote

sealed class StateResource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : StateResource<T>(data)
    class Error<T>(message: String, data: T? = null) : StateResource<T>(data, message)
    class Loading<T>(data: T? = null) : StateResource<T>(data)
}