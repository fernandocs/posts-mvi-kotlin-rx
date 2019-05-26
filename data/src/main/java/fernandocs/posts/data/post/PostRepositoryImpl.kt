package fernandocs.posts.data.post

import fernandocs.posts.data.post.dto.PostResponse
import fernandocs.posts.domain.posts.Post
import fernandocs.posts.domain.posts.PostsRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val service: PostService
) : PostsRepository {

    override fun getPost(postId: Int): Single<Post> =
        service.getPost(postId)
            .map(PostResponse::mapToDomain)
            .subscribeOn(Schedulers.io())

    override fun getAllPosts(): Single<List<Post>> =
        service.getPosts()
            .map {
                it.map(PostResponse::mapToDomain)
            }
            .subscribeOn(Schedulers.io())
}
