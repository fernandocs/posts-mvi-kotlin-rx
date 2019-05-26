package fernandocs.posts.data.user

import fernandocs.posts.data.user.dto.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

internal interface UserService {
    @GET("users/{id}")
    fun user(@Path("id") id: Int): Single<UserResponse>
}
