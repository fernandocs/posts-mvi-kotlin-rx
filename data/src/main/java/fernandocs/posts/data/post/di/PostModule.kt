package fernandocs.posts.data.post.di

import dagger.Module
import dagger.Provides
import fernandocs.posts.data.ApiClientFactory
import fernandocs.posts.data.post.PostRepositoryImpl
import fernandocs.posts.data.post.PostService
import fernandocs.posts.domain.posts.PostsRepository

@Module
internal class PostModule {

    @Provides
    fun providePostRepository(service: PostService): PostsRepository {
        return PostRepositoryImpl(service)
    }

    @Provides
    internal fun providePostService(apiClientFactory: ApiClientFactory): PostService {
        return apiClientFactory.retrofit.create(PostService::class.java)
    }
}