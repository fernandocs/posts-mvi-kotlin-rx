package fernandocs.posts.details

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import fernandocs.posts.domain.comments.Comment
import fernandocs.posts.domain.posts.Post
import fernandocs.posts.domain.users.User
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class DetailsViewModel @Inject constructor(): ViewModel() {
    var currentViewState: DetailsViewState = DetailsViewState()
}

@Parcelize
data class DetailsViewState(
    var postId: Int? = null,
    val content: ContentViewState = ContentViewState.Initial
) : Parcelable

sealed class ContentViewState : Parcelable {
    @Parcelize
    object Initial : ContentViewState()

    @Parcelize
    object Error : ContentViewState()

    @Parcelize
    object Loading : ContentViewState()

    @Parcelize
    data class Content(
        val userUserViewState: UserViewState? = null,
        val postViewState: PostViewState? = null,
        val commentsViewState: CommentsViewState? = null
    ) : ContentViewState()
}

@Parcelize
data class PostViewState(
    val title: String,
    val body: String
) : Parcelable

@Parcelize
data class UserViewState(
    val userPhotoUrl: String,
    val userName: String
) : Parcelable

@Parcelize
data class CommentsViewState(
    val commentsSize: Int
) : Parcelable

open class DetailsViewStateMapper @Inject constructor() {
    open fun transform(post: Post) = PostViewState(title = post.title, body = post.body)
    open fun transform(user: User) = UserViewState(
        userPhotoUrl = "https://api.adorable.io/avatars/${user.id}",
        userName = user.name
    )
    open fun transform(comments: List<Comment>) = CommentsViewState(commentsSize = comments.size)
}