package fernandocs.posts.domain.posts

import io.reactivex.Single

interface PostsRepository {
    fun getPost(postId: Int): Single<Post>

    fun getAllPosts(): Single<List<Post>>
}
