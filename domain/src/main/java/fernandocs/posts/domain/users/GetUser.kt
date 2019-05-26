package fernandocs.posts.domain.users

import javax.inject.Inject

open class GetUser @Inject constructor(
    private val repository: UserRepository
) {
    open fun execute(id: Int) = repository.getUser(id)
}
