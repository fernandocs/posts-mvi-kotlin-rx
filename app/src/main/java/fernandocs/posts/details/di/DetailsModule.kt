package fernandocs.posts.details.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import fernandocs.posts.details.DetailsFragment
import fernandocs.posts.details.DetailsView
import fernandocs.posts.details.DetailsViewModel
import fernandocs.posts.di.ViewModelKey

@Module(includes = [DetailsModule.ProvideViewModel::class])
abstract class DetailsModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    abstract fun contributeDetailsFragment(): DetailsFragment

    @Binds
    abstract fun bindDetailsView(fragment: DetailsFragment): DetailsView

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(DetailsViewModel::class)
        fun provideDetailsViewModel(): ViewModel = DetailsViewModel()
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideDetailsViewModel(
            factory: ViewModelProvider.Factory,
            target: DetailsFragment
        ): DetailsViewModel =
            ViewModelProviders.of(target, factory).get(DetailsViewModel::class.java)
    }
}
