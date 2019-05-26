package fernandocs.posts.details

import io.reactivex.Observable

interface DetailsView {
    fun getIntents(): Observable<DetailsIntent>
}