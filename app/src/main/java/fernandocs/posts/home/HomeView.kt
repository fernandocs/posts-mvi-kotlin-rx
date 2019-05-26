package fernandocs.posts.home

import io.reactivex.Observable

interface HomeView {
    fun getIntents(): Observable<HomeIntent>
}
