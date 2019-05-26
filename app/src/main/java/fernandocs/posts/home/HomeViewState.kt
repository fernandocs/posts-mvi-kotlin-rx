package fernandocs.posts.home

import android.os.Parcelable
import fernandocs.posts.domain.posts.Post
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Parcelize
data class HomeViewState(
    val content: ContentViewState = ContentViewState.Initial
) : Parcelable

sealed class ContentViewState : Parcelable {
    @Parcelize object Initial : ContentViewState()
    @Parcelize object Error : ContentViewState()
    @Parcelize object Empty : ContentViewState()
    @Parcelize object Loading : ContentViewState()
    @Parcelize data class Content(
        val postsViewState: List<PostViewState> = emptyList()
    ) : ContentViewState()
}

@Parcelize
data class PostViewState(
    val id: Int,
    val title: String
): Parcelable

open class PostViewStateMapper @Inject constructor() {
    open fun transform(post: Post) = PostViewState(id = post.id, title = post.title)
}
