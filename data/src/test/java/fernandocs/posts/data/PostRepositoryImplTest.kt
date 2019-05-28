package fernandocs.posts.data

import fernandocs.posts.data.post.PostRepositoryImpl
import fernandocs.posts.data.post.PostService
import fernandocs.posts.data.post.dto.PostResponse
import fernandocs.posts.domain.posts.Post
import fernandocs.posts.domain.posts.PostsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldStartWith
import org.junit.Before
import org.junit.Test

class PostRepositoryImplTest {

    @Test
    fun `Get All Posts - Returns list of post`() {
        // Given
        val postResponse = mockk<PostResponse>(relaxed = true)

        every { service.getPosts() } returns Single.just(listOf(postResponse))

        val postsResult = mockk<Post>()

        every { postResponse.mapToDomain() } returns postsResult
        var thread = ""

        // When
        val result = repository.getAllPosts()
            .doOnEvent { _, _ -> thread = Thread.currentThread().name }
            .blockingGet()

        // Then
        verify { service.getPosts() }
        verify { postResponse.mapToDomain() }
        result shouldEqual listOf(postsResult)
        thread shouldStartWith "RxCachedThreadScheduler"
    }

    @Test
    fun `Get Post By Id - Returns single post`() {
        // Given
        val postResponse = mockk<PostResponse>(relaxed = true)

        every { service.getPost(any()) } returns Single.just(postResponse)

        val postsResult = mockk<Post>()

        every { postResponse.mapToDomain() } returns postsResult
        var thread = ""

        // When
        val result = repository.getPost(1)
            .doOnEvent { _, _ -> thread = Thread.currentThread().name }
            .blockingGet()

        // Then
        verify { service.getPost(1) }
        verify { postResponse.mapToDomain() }
        result shouldEqual postsResult
        thread shouldStartWith "RxCachedThreadScheduler"
    }

    private lateinit var service: PostService
    private lateinit var repository: PostsRepository

    @Before
    fun setup() {
        service = mockk()
        repository = PostRepositoryImpl(service)
    }
}