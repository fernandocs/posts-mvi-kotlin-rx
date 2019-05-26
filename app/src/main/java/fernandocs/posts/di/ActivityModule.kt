package fernandocs.posts.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fernandocs.posts.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}