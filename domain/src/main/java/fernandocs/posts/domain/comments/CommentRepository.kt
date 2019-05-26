package fernandocs.posts.domain.comments

import io.reactivex.Single

interface CommentRepository {
    fun getComments(postId: Int): Single<List<Comment>>
}
