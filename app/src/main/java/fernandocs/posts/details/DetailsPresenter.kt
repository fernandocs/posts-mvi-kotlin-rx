package fernandocs.posts.details

import fernandocs.posts.domain.comments.Comment
import fernandocs.posts.domain.comments.GetComments
import fernandocs.posts.domain.posts.GetPost
import fernandocs.posts.domain.posts.Post
import fernandocs.posts.domain.users.GetUser
import fernandocs.posts.domain.users.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
    private val view: DetailsView,
    private val getPost: GetPost,
    private val getUser: GetUser,
    private val getComments: GetComments,
    private val mapper: DetailsViewStateMapper
) {

    fun getStates(viewState: DetailsViewState): Observable<DetailsViewState> {
        val initialReducer =
            if (viewState.content is ContentViewState.Initial) viewState.postId?.let {
                loadPost(it)
            } ?: Observable.just { oldState: DetailsViewState -> oldState.copy(content = ContentViewState.Error) }
            else Observable.empty()
        val intentReducer = view.getIntents().flatMap(::handleIntent)
        return initialReducer.concatWith(intentReducer)
            .scan(viewState) { previousState, reducer ->
                reducer(previousState)
            }.onErrorResumeNext { t: Throwable ->
                getStates(viewState.copy(content = ContentViewState.Error))
            }
    }

    private fun handleIntent(
        intent: DetailsIntent
    ): Observable<(DetailsViewState) -> DetailsViewState> {
        return Observable.empty()
    }

    private fun loadPost(postId: Int): Observable<(DetailsViewState) -> DetailsViewState> {
        fun getOnErrorReducer(): (DetailsViewState) -> DetailsViewState =
            { oldState: DetailsViewState ->
                oldState.copy(content = ContentViewState.Error)
            }

        fun getOnSubmitRequestReducer(): (DetailsViewState) -> DetailsViewState =
            { oldState: DetailsViewState ->
                oldState.copy(
                    content = ContentViewState.Loading
                )
            }

        val commentsObservable = getComments
            .execute(postId)
            .observeOn(AndroidSchedulers.mainThread())
            .map { comments: List<Comment> ->
                { oldState: DetailsViewState ->
                    when (oldState.content) {
                        is ContentViewState.Content -> oldState.copy(
                            content = oldState.content.copy(
                                commentsViewState = mapper.transform(comments)
                            )
                        )
                        else -> oldState.copy(
                            content = ContentViewState.Content(
                                commentsViewState = mapper.transform(comments)
                            )
                        )
                    }
                }
            }
            .toObservable()

        return getPost.execute(postId)
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .flatMap { post: Post ->
                getUser.execute(post.userId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { user: User ->
                        { oldState: DetailsViewState ->
                            when (oldState.content) {
                                is ContentViewState.Content -> oldState.copy(
                                    content = oldState.content.copy(
                                        userUserViewState = mapper.transform(user),
                                        postViewState = mapper.transform(post)
                                    )
                                )
                                else -> oldState.copy(
                                    content = ContentViewState.Content(
                                        userUserViewState = mapper.transform(user),
                                        postViewState = mapper.transform(post)
                                    )
                                )
                            }
                        }
                    }
                    .toObservable()
            }
            .onErrorReturn { getOnErrorReducer() }
            .mergeWith(commentsObservable)
            .startWith(getOnSubmitRequestReducer())
    }

}