package fernandocs.posts.home

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import fernandocs.posts.R
import kotlinx.android.synthetic.main.item_post.*

class PostItem(val viewState: PostViewState) : Item() {
    override fun getLayout() = R.layout.item_post

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            textViewTitle.text = viewState.title
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?) =
        other is PostItem && this.viewState.id == other.viewState.id
}
