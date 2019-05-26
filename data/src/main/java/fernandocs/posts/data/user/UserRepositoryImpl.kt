package fernandocs.posts.data.user

import fernandocs.posts.data.user.dto.UserResponse
import fernandocs.posts.domain.users.User
import fernandocs.posts.domain.users.UserRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val service: UserService
) : UserRepository {

    override fun getUser(id: Int): Single<User> =
        service.user(id)
            .map(UserResponse::mapToDomain)
            .subscribeOn(Schedulers.io())
}
