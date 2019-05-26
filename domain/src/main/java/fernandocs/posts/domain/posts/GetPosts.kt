package fernandocs.posts.domain.posts

import javax.inject.Inject

open class GetPosts @Inject constructor(
    private val repository: PostsRepository
) {
    open fun execute() = repository.getAllPosts()
}
