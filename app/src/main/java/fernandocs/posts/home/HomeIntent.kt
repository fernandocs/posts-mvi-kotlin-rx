package fernandocs.posts.home

sealed class HomeIntent {
    data class PostTapped(val postId: Int) : HomeIntent()
    object Refresh: HomeIntent()
}
