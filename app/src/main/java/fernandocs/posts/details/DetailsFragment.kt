package fernandocs.posts.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import fernandocs.posts.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : Fragment(), DetailsView {

    @Inject
    lateinit var presenter: DetailsPresenter

    private val disposable = CompositeDisposable()
    private lateinit var currentViewState: DetailsViewState
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        savedInstanceState?.getParcelable<DetailsViewState>(KEY_STATE)
            ?.let { currentViewState = it }

        if (!::currentViewState.isInitialized) currentViewState = DetailsViewState(postId = args.postId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.getStates(currentViewState)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)
            .addTo(disposable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_STATE, currentViewState)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

    override fun getIntents(): Observable<DetailsIntent> = Observable.empty()

    private fun render(viewState: DetailsViewState) {
        currentViewState = viewState
        when (viewState.content) {
            is ContentViewState.Content -> {
                viewState.content.userUserViewState?.let {
                    Glide.with(this).load(it.userPhotoUrl).centerCrop().into(imageViewUserPhoto)
                    textViewUserName.text = it.userName
                }
                viewState.content.postViewState?.let {
                    textViewPostTitle.text = it.title
                    textViewPostBody.text = it.body
                }
                viewState.content.commentsViewState?.let {
                    textViewCommentsSize.text = activity?.resources?.getQuantityString(
                        R.plurals.comments_size,
                        it.commentsSize, it.commentsSize.toString()
                    )
                }
            }
        }
    }

    companion object {
        private const val KEY_STATE = "KEY_STATE"
    }
}
