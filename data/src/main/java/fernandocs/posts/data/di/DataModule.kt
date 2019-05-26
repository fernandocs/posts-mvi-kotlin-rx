package fernandocs.posts.data.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import fernandocs.posts.data.ApiClientFactory
import fernandocs.posts.data.BuildConfig
import fernandocs.posts.data.comment.di.CommentModule
import fernandocs.posts.data.post.di.PostModule
import fernandocs.posts.data.user.di.UserModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(
    includes = [PostModule::class, UserModule::class, CommentModule::class]
)
class DataModule {
    @Provides
    internal fun provideApiClientFactory(
        @Named("BASE_URL") url: String
    ): ApiClientFactory {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) BASIC else NONE
        }
        val okHttp = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        return ApiClientFactory(
            Retrofit.Builder()
                .baseUrl(url)
                .client(okHttp)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        )
    }
}
