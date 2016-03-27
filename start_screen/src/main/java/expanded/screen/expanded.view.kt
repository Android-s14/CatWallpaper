package expanded.screen

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import butterknife.bindView
import com.android_s14.catwallpaper.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import shared.*
import start.screen.Const
import javax.inject.Inject

class ExpandedView : BaseView<String>() {

  @Inject override lateinit var presenter: Presenter<String>
  override val component by lazy { applicationComponent.plus(ExpandedViewModule(this)) }
  @Inject lateinit var picasso : Picasso

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val loading by bindView<android.view.View>(R.id.loading_indicator)
  private val imageView by bindView<ImageView>(R.id.expanded_image_view)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.expanded_view_screen)
    setSupportActionBar(toolbar)
    component.inject(this)
    loadImage()
  }

  private fun loadImage() {
    picasso.load(getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(imageView, object : Callback {
      override fun onSuccess() {
        hideLoading()
      }

      override fun onError() {
        hideLoading()
      }
    })
  }

  override fun showLoading() = loading.show()

  override fun hideLoading() = loading.hide()

  override fun updateData(newData: Collection<String>) {
    throw UnsupportedOperationException()
  }

  private fun getImageUrl() = intent.getStringExtra(Const.INTENT_EXTRA_IMAGE_URL)

}

class ExpandedPresenter(override val view: View<String>) : Presenter<String> {

  override fun onViewCreated() {
    throw UnsupportedOperationException()
  }

  override fun onViewDestroyed() {
    throw UnsupportedOperationException()
  }

  override fun updateView() {
    throw UnsupportedOperationException()
  }
}

@PerActivity
@dagger.Subcomponent(modules = arrayOf(
    ExpandedViewModule::class
))
interface ExpandedComponent : Component {

  fun inject(target: ExpandedView)

}

@Module
class ExpandedViewModule(private val view: View<String>) {

  @Provides @PerActivity fun expandedView(): View<String> = view

  @Provides @PerActivity fun expandedPresenter(): Presenter<String> = ExpandedPresenter(view)

}