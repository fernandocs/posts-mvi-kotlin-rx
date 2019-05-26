package fernandocs.posts.domain.users

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GetUserTest {

    @Test
    fun `GetUser use case returns correct user`() {
        val repo = mock<UserRepository> {
            on { getUser(any()) } doReturn Single.just(user)
        }

        val useCase = GetUser(repo)

        // When
        val result = useCase.execute("1").blockingGet()

        result shouldEqual user
    }

    companion object {
        val user = User(id = "1", name = "Fernando", email = "fernando@email.com")
    }
}