package start.screen

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.android_s14.catwallpaper.R
import dagger.Module
import dagger.Provides
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import shared.*
import javax.inject.Inject

class StartView : BaseView<ViewModel>() {

  @Inject override lateinit var presenter: Presenter<ViewModel>
  override val component: StartComponent by lazy { applicationComponent.plus(ViewModule(this)) }

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val loadingSpinner by bindView<android.view.View>(R.id.loading_indicator)
  private val listView by bindView<RecyclerView>(R.id.recycler_view)

  private val adapter = ListAdapter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    setupView(savedInstanceState)

    component.inject(this)
    presenter.onViewCreated()
  }

  override fun onDestroy() {
    super.onDestroy()

  }

  private fun setupView(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.start_screen)
    setSupportActionBar(toolbar)

    listView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    listView.itemAnimator = DefaultItemAnimator()
    listView.adapter = adapter
  }

  override fun showLoading() = loadingSpinner.show()

  override fun hideLoading() = loadingSpinner.hide()

  override fun updateData(newData: Collection<ViewModel>) = adapter.swapData(newData)

}

class StartPresenter(override val view: View<ViewModel>) : Presenter<ViewModel> {

  @Inject lateinit var imageLoader: Interactor<Nothing, Collection<ViewModel>>

  private val subscription = CompositeSubscription()

  init {
    (view.component as StartComponent).inject(this)
  }

  override fun onViewCreated() {
    imageLoader.execute()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          view.updateData(it)
          view.hideLoading()
        }.let { subscription.add(it) }
  }

  override fun onViewDestroyed() = subscription.clear()

}

class ImageUrlsFetcher(view: View<ViewModel>) : Interactor<Nothing, Collection<ViewModel>> {

  @Inject lateinit var repository: Repository<Collection<ViewModel>>

  init {
    (view.component as StartComponent).inject(this)
  }

  override fun execute(vararg input: Nothing) = repository.getData()

}

data class ViewModel(val imageId: Long, val imageUrl: String) {
  constructor(imageId: String, imageUrl: String) : this(imageId.hashCode().toLong(), imageUrl)
}

@Module
class ViewModule(val view: View<ViewModel>) {

  @Provides @PerActivity fun view() = view

  @Provides @PerActivity fun presenter(injectedView: View<ViewModel>): Presenter<ViewModel> = StartPresenter(injectedView)

}

@Module
class InteractorModule {

  @Provides @PerActivity fun interactor(view: View<ViewModel>): Interactor<Nothing, Collection<ViewModel>> {
    return ImageUrlsFetcher(view)
  }

}

@Module
class StartRepositoryModule {

  @Provides @PerActivity fun repository(view: View<ViewModel>): Repository<Collection<ViewModel>> {
    return CatsRepository(view)
  }

}

@PerActivity
@dagger.Subcomponent(modules = arrayOf(
    ViewModule::class,
    InteractorModule::class,
    StartRepositoryModule::class
))
interface StartComponent : Component {
  fun inject(target: StartView)
  fun inject(target: StartPresenter)
  fun inject(target: ImageUrlsFetcher)
}