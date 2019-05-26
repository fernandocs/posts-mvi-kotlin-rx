package fernandocs.posts.data.comment

import fernandocs.posts.data.comment.dto.CommentResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CommentService {
    @GET("comments")
    fun comments(@Query("postId") id: Int): Single<List<CommentResponse>>
}
