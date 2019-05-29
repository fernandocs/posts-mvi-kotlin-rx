package fernandocs.posts.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.AndroidSupportInjection
import fernandocs.posts.ItemClickObservable
import fernandocs.posts.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeView {

    @Inject
    lateinit var presenter: HomePresenter

    @Inject
    lateinit var viewModel: HomeViewModel

    private val disposable = CompositeDisposable()
    private val adapter = GroupAdapter<ViewHolder>()
    private val intents = PublishSubject.create<HomeIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            viewPostsContent.layoutManager = LinearLayoutManager(activity)
            viewPostsContent.adapter = adapter

            viewPostsLoading.setOnRefreshListener { intents.onNext(HomeIntent.Refresh) }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.getStates(viewModel.currentViewState)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)
            .addTo(disposable)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

    override fun getIntents(): Observable<HomeIntent> {
        val itemClickIntents = ItemClickObservable(adapter)
            .ofType(PostItem::class.java)
            .map { HomeIntent.PostTapped(it.viewState.id) }
        return Observable.merge(itemClickIntents, intents)
    }

    private fun render(viewState: HomeViewState) {
        viewModel.currentViewState = viewState
        viewPostsLoading.isRefreshing = viewState.content == ContentViewState.Loading
        when (viewState.content) {
            is ContentViewState.Content -> adapter.update(viewState.content.postsViewState.map(::PostItem))
            is ContentViewState.Empty -> {
                Toast.makeText(activity, R.string.empty_message, Toast.LENGTH_SHORT).show()
            }
            is ContentViewState.Error -> {
                Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
