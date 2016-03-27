package start.screen

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import shared.Interactor
import shared.Presenter
import shared.View
import javax.inject.Inject

class StartPresenter(override val view: View<ViewModel>) : Presenter<ViewModel> {

  @Inject @field:[InteractorModule.OnlineUrlsFetcher] lateinit var onlineUrlsFetcher: Interactor<Nothing, Collection<ViewModel>>
  @Inject @field:[InteractorModule.DbUrlsFetcher] lateinit var dbUrlsFetcher: Interactor<Nothing, Collection<ViewModel>>
  @Inject @field:[InteractorModule.DbUrlsSaver] lateinit var dbUrlsSaver: Interactor<ViewModel, Boolean>

  private val subscription = CompositeSubscription()

  init {
    (view.component as StartComponent).inject(this)
  }

  override fun onViewCreated() = getImageUrlsFromDb()

  override fun onViewDestroyed() = subscription.clear()

  override fun updateView() = getNewImageUrls()

  private fun getNewImageUrls() = onlineUrlsFetcher.execute().updateUi(true)

  private fun getImageUrlsFromDb() = dbUrlsFetcher.execute().updateUi()

  private fun Observable<Collection<ViewModel>>.updateUi(andSaveToDb: Boolean = false) {
    this.observeOn(AndroidSchedulers.mainThread())
        .doOnNext { if (andSaveToDb) dbUrlsSaver.execute(*it.toTypedArray()) }
        .subscribe {
          view.updateData(it)
          view.hideLoading()
        }.let { subscription.add(it) }
  }
}