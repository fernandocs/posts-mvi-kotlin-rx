package fernandocs.posts.data

import fernandocs.posts.data.user.UserRepositoryImpl
import fernandocs.posts.data.user.UserService
import fernandocs.posts.data.user.dto.UserResponse
import fernandocs.posts.domain.users.User
import fernandocs.posts.domain.users.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldStartWith
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    @Test
    fun `Get User by UserId - Returns single user`() {
        // Given
        val userResponse = mockk<UserResponse>(relaxed = true)

        every { service.user(any()) } returns Single.just(userResponse)

        val userResult = mockk<User>()

        every { userResponse.mapToDomain() } returns userResult
        var thread = ""

        // When
        val result = repository.getUser(1)
            .doOnEvent { _, _ -> thread = Thread.currentThread().name }
            .blockingGet()

        // Then
        verify { service.user(1) }
        verify { userResponse.mapToDomain() }
        result shouldEqual userResult
        thread shouldStartWith "RxCachedThreadScheduler"
    }

    private lateinit var service: UserService
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        service = mockk()
        repository = UserRepositoryImpl(service)
    }

}