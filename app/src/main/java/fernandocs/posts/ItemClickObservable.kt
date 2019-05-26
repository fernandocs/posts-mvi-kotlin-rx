package fernandocs.posts

import android.os.Looper
import android.view.View
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposables

class ItemClickObservable(
    private val adapter: GroupAdapter<ViewHolder>
) : Observable<Item<*>>() {

    override fun subscribeActual(observer: Observer<in Item<*>>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(adapter, observer)
        observer.onSubscribe(listener)
        adapter.setOnItemClickListener(listener)
    }

    internal class Listener(
        private val adapter: GroupAdapter<ViewHolder>,
        private val observer: Observer<in Item<*>>
    ) :
        MainThreadDisposable(), OnItemClickListener {
        override fun onItemClick(item: Item<*>, view: View) {
            if (!isDisposed) {
                observer.onNext(item)
            }
        }

        override fun onDispose() {
            adapter.setOnItemClickListener(null)
        }
    }

    private fun checkMainThread(observer: Observer<in Item<*>>): Boolean {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty())
            observer.onError(
                IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().name
                )
            )
            return false
        }
        return true
    }
}
