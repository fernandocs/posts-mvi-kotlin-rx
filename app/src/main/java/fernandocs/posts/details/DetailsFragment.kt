package fernandocs.posts.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    @Inject
    lateinit var viewModel: DetailsViewModel

    private val disposable = CompositeDisposable()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        viewModel.currentViewState.postId ?: run { viewModel.currentViewState.postId = args.postId }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_details, container, false)
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

    override fun getIntents(): Observable<DetailsIntent> = Observable.empty()

    private fun render(viewState: DetailsViewState) {
        viewModel.currentViewState = viewState

        progressBar.visibility = if (viewState.content is ContentViewState.Loading) { View.VISIBLE } else { View.GONE }

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
            is ContentViewState.Error -> {
                Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
