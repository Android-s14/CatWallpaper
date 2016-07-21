package expanded.screen

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.android_s14.catwallpaper.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import shared.BaseView
import shared.bindView
import shared.hide
import shared.show
import javax.inject.Inject

class ExpandedView : BaseView<String>() {

  @Inject override lateinit var presenter: ExpandedPresenter
  override val component by lazy { applicationComponent.plus(ExpandedViewModule(this)) }

  @Inject lateinit var picasso: Picasso

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val loading by bindView<View>(R.id.loading_indicator)
  private val imageView by bindView<ImageView>(R.id.expanded_image_view)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.expanded_view_screen)
    setSupportActionBar(toolbar)

    component.inject(this)

    window.enterTransition = null
    presenter.onViewCreated()
  }

  fun loadImage(imageUrl: String) {
    picasso.load(imageUrl)
        .fit()
        .centerInside()
        .into(imageView, object : Callback {
          override fun onSuccess() = presenter.imageLoaded()
          override fun onError() = presenter.imageFailed()
        })
  }

  override fun showLoading() = loading.show()

  override fun hideLoading() = loading.hide()

  override fun updateData(newData: Collection<String>) = throw UnsupportedOperationException()

}