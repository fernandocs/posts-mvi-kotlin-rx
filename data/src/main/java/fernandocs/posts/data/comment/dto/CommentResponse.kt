package fernandocs.posts.data.comment.dto

import fernandocs.posts.domain.comments.Comment

internal data class CommentResponse(
    val id: Int,
    val postId: String,
    val name: String,
    val email: String,
    val body: String
) {
    internal fun mapToDomain() = Comment(
        id = id,
        postId = postId,
        name = name,
        email = email,
        body = body
    )
}
