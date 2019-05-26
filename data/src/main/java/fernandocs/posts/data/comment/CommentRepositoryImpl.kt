package fernandocs.posts.data.comment

import fernandocs.posts.data.comment.dto.CommentResponse
import fernandocs.posts.domain.comments.Comment
import fernandocs.posts.domain.comments.CommentRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class CommentRepositoryImpl @Inject constructor(
    private val service: CommentService
) : CommentRepository {

    override fun getComments(postId: Int): Single<List<Comment>> =
        service.comments(postId)
            .map { it.map(CommentResponse::mapToDomain) }
            .subscribeOn(Schedulers.io())
}
