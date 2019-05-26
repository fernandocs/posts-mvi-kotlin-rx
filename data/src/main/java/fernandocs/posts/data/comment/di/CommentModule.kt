package fernandocs.posts.data.comment.di

import dagger.Module
import dagger.Provides
import fernandocs.posts.data.ApiClientFactory
import fernandocs.posts.data.comment.CommentRepositoryImpl
import fernandocs.posts.data.comment.CommentService
import fernandocs.posts.domain.comments.CommentRepository

@Module
internal class CommentModule {

    @Provides
    fun provideCommentRepository(service: CommentService): CommentRepository {
        return CommentRepositoryImpl(service)
    }

    @Provides
    internal fun provideCommentService(apiClientFactory: ApiClientFactory): CommentService {
        return apiClientFactory.retrofit.create(CommentService::class.java)
    }
}
