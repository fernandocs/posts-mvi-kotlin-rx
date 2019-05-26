package fernandocs.posts.domain.post

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import fernandocs.posts.domain.posts.GetPosts
import fernandocs.posts.domain.posts.Post
import fernandocs.posts.domain.posts.PostsRepository
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GetPostsTest {
    @Test
    fun `GetPosts use case returns correct list of post`() {
        val repo = mock<PostsRepository> {
            on { getAllPosts() } doReturn Single.just(posts)
        }

        val useCase = GetPosts(repo)

        val result = useCase.execute().blockingGet()

        result shouldEqual posts
    }

    companion object {
        private val post = Post(id = "1", title = "Title", body = "Body", userId = "1")
        val posts = listOf(post)
    }
}