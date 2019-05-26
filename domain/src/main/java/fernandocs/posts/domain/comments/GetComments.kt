package fernandocs.posts.domain.comments

import javax.inject.Inject

open class GetComments @Inject constructor(
    private val repository: CommentRepository
) {
    open fun execute(postId: Int) = repository.getComments(postId)
}
