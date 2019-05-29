package fernandocs.posts.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import fernandocs.posts.di.ViewModelKey
import fernandocs.posts.home.*

@Module(includes = [HomeModule.ProvideViewModel::class])
abstract class HomeModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @Binds
    abstract fun bindHomeView(fragment: HomeFragment): HomeView

    @Binds
    abstract fun bindHomeNavigator(navigator: HomeNavigatorImpl): HomeNavigator

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(HomeViewModel::class)
        fun provideHomeViewModel(): ViewModel = HomeViewModel()
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideHomeViewModel(
            factory: ViewModelProvider.Factory,
            target: HomeFragment
        ): HomeViewModel =
            ViewModelProviders.of(target, factory).get(HomeViewModel::class.java)
    }
}
