package fernandocs.posts.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import fernandocs.posts.App
import fernandocs.posts.data.di.DataModule
import javax.inject.Named
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DataModule::class,
        AppModule::class,
        ActivityModule::class,
        FragmentModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(instance: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(@Named("BASE_URL") url: String): Builder

        fun build(): AppComponent
    }
}
