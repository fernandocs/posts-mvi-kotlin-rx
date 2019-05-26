package fernandocs.posts.data.user.di

import dagger.Module
import dagger.Provides
import fernandocs.posts.data.ApiClientFactory
import fernandocs.posts.data.user.UserRepositoryImpl
import fernandocs.posts.data.user.UserService
import fernandocs.posts.domain.users.UserRepository

@Module
internal class UserModule {

    @Provides
    fun providePostRepository(service: UserService): UserRepository {
        return UserRepositoryImpl(service)
    }

    @Provides
    internal fun providePostService(apiClientFactory: ApiClientFactory): UserService {
        return apiClientFactory.retrofit.create(UserService::class.java)
    }
}