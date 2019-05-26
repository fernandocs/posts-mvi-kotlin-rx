package fernandocs.posts.data.user.dto

import fernandocs.posts.domain.users.User

internal data class UserResponse(
    val id: Int,
    val name: String,
    val email: String
) {
    internal fun mapToDomain() = User(
        id = id,
        name = name,
        email = email
    )
}