package fernandocs.posts.data

import fernandocs.posts.data.comment.CommentRepositoryImpl
import fernandocs.posts.data.comment.CommentService
import fernandocs.posts.data.comment.dto.CommentResponse
import fernandocs.posts.domain.comments.Comment
import fernandocs.posts.domain.comments.CommentRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldStartWith
import org.junit.Before
import org.junit.Test

class CommentRepositoryImplTest {

    @Test
    fun `Get Comments by PostId - Returns list of comment`() {
        // Given
        val commentsResponse = mockk<CommentResponse>(relaxed = true)

        every { service.comments(any()) } returns Single.just(listOf(commentsResponse))

        val commentsResult = mockk<Comment>()

        every { commentsResponse.mapToDomain() } returns commentsResult
        var thread = ""

        // When
        val result = repository.getComments(1)
            .doOnEvent { _, _ -> thread = Thread.currentThread().name }
            .blockingGet()

        // Then
        verify { service.comments(1) }
        verify { commentsResponse.mapToDomain() }
        result shouldEqual listOf(commentsResult)
        thread shouldStartWith "RxCachedThreadScheduler"
    }

    private lateinit var service: CommentService
    private lateinit var repository: CommentRepository

    @Before
    fun setup() {
        service = mockk()
        repository = CommentRepositoryImpl(service)
    }

}