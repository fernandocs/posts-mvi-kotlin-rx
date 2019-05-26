package fernandocs.posts.domain.comment

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import fernandocs.posts.domain.comments.Comment
import fernandocs.posts.domain.comments.CommentRepository
import fernandocs.posts.domain.comments.GetComments
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GetCommentsTest {
    @Test
    fun `GetComments use case returns correct list of comments`() {
        val repo = mock<CommentRepository> {
            on { getComments(any()) } doReturn Single.just(comments)
        }

        val useCase = GetComments(repo)

        // When
        val result = useCase.execute("1").blockingGet()

        result shouldEqual comments
    }

    companion object {
        private val comment = Comment(
            id = "1",
            postId = "Fernando",
            name = "Fernando",
            email = "fernando@email.com",
            body = "Body"
        )
        val comments = listOf(comment)
    }
}