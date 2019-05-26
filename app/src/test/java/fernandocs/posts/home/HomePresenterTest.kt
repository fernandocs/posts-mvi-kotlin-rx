package fernandocs.posts.home

import fernandocs.posts.TestSchedulerConfigurator
import fernandocs.posts.domain.posts.GetPosts
import fernandocs.posts.domain.posts.Post
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomePresenterTest {
    private val testSchedulerConfigurator = TestSchedulerConfigurator()
    private val testScheduler = testSchedulerConfigurator.scheduler

    @Test
    fun `Screen is on initialization`() {
        testObserver = presenter.getStates(HomeViewState()).test()

        // Then
        testScheduler.triggerActions()
        testObserver
            .assertValueAt(0) { state -> state.content is ContentViewState.Initial }
        testObserver
            .assertValueAt(1) { state -> state.content is ContentViewState.Loading }
        testObserver
            .assertValueAt(2) { state ->
                state.content is ContentViewState.Content
                        && (state.content as ContentViewState.Content).postsViewState.size == 2
            }
    }

    @Test
    fun `Tapped item navigate to another screen`() {
        testObserver =
            presenter.getStates(HomeViewState(content = ContentViewState.Content(listOf(posViewState)))).test()

        // When
        intentsSubject.onNext(HomeIntent.PostTapped(postId = 1))

        // Then
        testScheduler.triggerActions()
        verify { navigator.navigateToDetails(postId = 1) }
    }

    @Test
    fun `Refresh screen`() {
        testObserver =
            presenter.getStates(HomeViewState(content = ContentViewState.Content(listOf(posViewState)))).test()

        // When
        intentsSubject.onNext(HomeIntent.Refresh)

        // Then
        testScheduler.triggerActions()
        testObserver
            .assertValueAt(0) { state -> state.content is ContentViewState.Content }
        testObserver
            .assertValueAt(1) { state -> state.content is ContentViewState.Loading }
        testObserver
            .assertValueAt(2) { state ->
                state.content is ContentViewState.Content
                        && (state.content as ContentViewState.Content).postsViewState.size == 2
            }
    }

    private lateinit var intentsSubject: PublishSubject<HomeIntent>
    private lateinit var testObserver: TestObserver<HomeViewState>
    private lateinit var presenter: HomePresenter
    private lateinit var view: HomeView
    private lateinit var getPosts: GetPosts
    private lateinit var navigator: HomeNavigator
    private lateinit var mapper: PostViewStateMapper

    @Before
    fun beforeTest() {
        testSchedulerConfigurator.setSchedulers()

        intentsSubject = PublishSubject.create()

        view = mockk(relaxed = true) {
            every { getIntents() } returns intentsSubject
        }

        getPosts = mockk {
            every { execute() } returns Single.just(listOf(post1, post2))
        }

        mapper = mockk(relaxed = true)

        navigator = mockk(relaxed = true)

        presenter = HomePresenter(getPosts, mapper, view, navigator)
    }

    @After
    fun afterTest() {
        testSchedulerConfigurator.resetSchedulers()
        testObserver.dispose()
    }

    companion object {
        val post1 = Post(
            id = 1,
            title = "title",
            body = "body",
            userId = 1
        )
        val post2 = Post(
            id = 2,
            title = "title",
            body = "body",
            userId = 1
        )
        val posViewState = PostViewState(id = 1, title = "title")
    }
}