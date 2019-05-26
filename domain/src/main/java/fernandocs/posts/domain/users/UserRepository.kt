package fernandocs.posts.domain.users

import io.reactivex.Single

interface UserRepository {
    fun getUser(id: Int): Single<User>
}
