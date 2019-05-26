package fernandocs.posts.domain.comments

data class Comment(
    val id: Int,
    val postId: String,
    val name: String,
    val email: String,
    val body: String
)
