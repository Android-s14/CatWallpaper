package expanded.screen

import shared.PerActivity
import shared.Presenter
import javax.inject.Inject

@PerActivity
class ExpandedPresenter
@Inject constructor(private val view: ExpandedView,
                    @ImageUrl private val imageUrl: String) : Presenter {

  override fun onViewCreated() = view.loadImage(imageUrl)
  override fun onViewDestroyed() = throw UnsupportedOperationException()
  override fun updateView() = throw UnsupportedOperationException()

  fun imageLoaded() = view.hideLoading()
  fun imageFailed() = view.hideLoading()

}