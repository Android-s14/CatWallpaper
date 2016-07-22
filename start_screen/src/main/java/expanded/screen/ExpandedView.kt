package expanded.screen

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.android_s14.catwallpaper.R
import shared.*
import javax.inject.Inject

class ExpandedView : BaseView<String>() {

  @Inject override lateinit var presenter: Presenter
  override val component by lazy { applicationComponent.plus(ExpandedViewModule(this)) }

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val loading by bindView<View>(R.id.loading_indicator)
  val imageView by bindView<ImageView>(R.id.expanded_image_view)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.expanded_view_screen)
    setSupportActionBar(toolbar)

    component.inject(this)

    window.enterTransition = null
    presenter.onViewCreated()
  }

  override fun showLoading() = loading.show()
  override fun hideLoading() = loading.hide()
  override fun updateData(newData: Collection<String>) = presenter.updateView()
}