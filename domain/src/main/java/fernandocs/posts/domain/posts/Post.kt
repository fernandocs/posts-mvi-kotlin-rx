package fernandocs.posts.domain.posts

data class Post (
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)
