package fernandocs.posts.home

import androidx.navigation.fragment.findNavController
import javax.inject.Inject

interface HomeNavigator {
    fun navigateToDetails(postId: Int)
}

open class HomeNavigatorImpl @Inject constructor(
    private val fragment: HomeFragment
) : HomeNavigator {
    override fun navigateToDetails(postId: Int) {
        fragment.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(postId))
    }

}