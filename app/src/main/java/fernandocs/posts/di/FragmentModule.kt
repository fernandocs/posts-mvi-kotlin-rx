package fernandocs.posts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import fernandocs.posts.AppViewModelFactory
import fernandocs.posts.details.di.DetailsModule
import fernandocs.posts.home.di.HomeModule
import javax.inject.Provider

@Module(includes = [HomeModule::class, DetailsModule::class])
class FragmentModule {

    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory = AppViewModelFactory(providers)
}
