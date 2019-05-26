package fernandocs.posts.home

import fernandocs.posts.domain.posts.GetPosts
import fernandocs.posts.domain.posts.Post
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val getPosts: GetPosts,
    private val mapper: PostViewStateMapper,
    private val view: HomeView,
    private val navigator: HomeNavigator
) {

    fun getStates(viewState: HomeViewState): Observable<HomeViewState> {
        val initialReducer =
            if (viewState.content is ContentViewState.Initial) loadPosts()
            else Observable.empty()
        val intentReducer = view.getIntents().flatMap(::handleIntent)
        return initialReducer.concatWith(intentReducer)
            .scan(viewState) { previousState, reducer ->
                reducer(previousState)
            }.onErrorResumeNext { t: Throwable ->
                getStates(HomeViewState(content = ContentViewState.Error))
            }
    }

    private fun handleIntent(
        intent: HomeIntent
    ): Observable<(HomeViewState) -> HomeViewState> {
        return when (intent) {
            is HomeIntent.PostTapped -> handlePostTapped(intent)
            is HomeIntent.Refresh -> loadPosts()
        }
    }

    private fun handlePostTapped(
        intent: HomeIntent.PostTapped
    ): Observable<(HomeViewState) -> HomeViewState> {
        return Observable.empty<(HomeViewState) -> HomeViewState>()
            .doOnComplete {
                navigator.navigateToDetails(intent.postId)
            }
    }

    private fun loadPosts(): Observable<(HomeViewState) -> HomeViewState> {
        fun getOnSubmitRequestReducer(): (HomeViewState) -> HomeViewState =
            { oldState: HomeViewState ->
                oldState.copy(
                    content = ContentViewState.Loading
                )
            }

        fun getOnSuccessReducer(result: List<Post>): (HomeViewState) -> HomeViewState =
            { oldState ->
                oldState.copy(
                    content = if (result.isNotEmpty()) {
                        ContentViewState.Content(result.map { mapper.transform(it) })
                    } else {
                        ContentViewState.Empty
                    }
                )
            }

        return getPosts.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .map(::getOnSuccessReducer)
            .toObservable()
            .startWith(getOnSubmitRequestReducer())
    }
}