package fernandocs.posts.data.post

import fernandocs.posts.data.post.dto.PostResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface PostService {
    @GET("posts")
    fun getPosts(): Single<List<PostResponse>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: Int): Single<PostResponse>
}
