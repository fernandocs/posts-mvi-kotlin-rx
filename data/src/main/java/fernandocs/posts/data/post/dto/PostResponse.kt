package fernandocs.posts.data.post.dto

import fernandocs.posts.domain.posts.Post

internal data class PostResponse(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
) {
    internal fun mapToDomain() = Post(
        id = id,
        title = title,
        body = body,
        userId = userId
    )
}
