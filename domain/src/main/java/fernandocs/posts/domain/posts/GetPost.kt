package fernandocs.posts.domain.posts

import javax.inject.Inject

open class GetPost @Inject constructor(
    private val repository: PostsRepository
) {
    open fun execute(postId: Int) = repository.getPost(postId)
}
