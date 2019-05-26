package fernandocs.posts.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import fernandocs.posts.details.DetailsFragment
import fernandocs.posts.details.DetailsView
import fernandocs.posts.home.HomeFragment
import fernandocs.posts.home.HomeNavigator
import fernandocs.posts.home.HomeNavigatorImpl
import fernandocs.posts.home.HomeView

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @Binds
    abstract fun bindHomeView(fragment: HomeFragment): HomeView

    @Binds
    abstract fun bindHomeNavigator(navigator: HomeNavigatorImpl): HomeNavigator

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment

    @Binds
    abstract fun bindDetailsView(fragment: DetailsFragment): DetailsView
}