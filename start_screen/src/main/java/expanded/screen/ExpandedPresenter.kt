package expanded.screen

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import shared.PerActivity
import shared.Presenter
import shared.View
import javax.inject.Inject

@PerActivity
class ExpandedPresenter
@Inject constructor(private val view: View<String>,
                    @TargetImageView private val targetImageView: ImageView,
                    @ImageUrl private val imageUrl: String,
                    private val picasso: Picasso) : Presenter {

  override fun onViewCreated() = loadImage()
  override fun onViewDestroyed() = stopLoading()
  override fun updateView() = loadImage()

  private fun stopLoading() {
    picasso.cancelRequest(targetImageView)
  }

  private fun loadImage() {
    picasso.load(imageUrl).fit().centerInside().into(targetImageView, object : Callback {
      override fun onSuccess() = view.hideLoading()
      override fun onError() = view.hideLoading()
    })
  }
}