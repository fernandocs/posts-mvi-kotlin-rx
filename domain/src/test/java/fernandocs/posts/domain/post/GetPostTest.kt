package fernandocs.posts.domain.post

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import fernandocs.posts.domain.posts.GetPost
import fernandocs.posts.domain.posts.Post
import fernandocs.posts.domain.posts.PostsRepository
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GetPostTest {

    @Test
    fun `GetPost use case returns correct post`() {
        val repo = mock<PostsRepository> {
            on { getPost(any()) } doReturn Single.just(post)
        }

        val useCase = GetPost(repo)

        // When
        val result = useCase.execute("1").blockingGet()

        result shouldEqual post
    }

    companion object {
        val post = Post(id = "1", title = "Title", body = "Body", userId = "1")
    }
}