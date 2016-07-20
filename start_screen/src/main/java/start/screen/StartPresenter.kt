package start.screen

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.SerialSubscription
import shared.PerActivity
import shared.Presenter
import javax.inject.Inject

@PerActivity
class StartPresenter
@Inject constructor(private val view: StartView,
                    private val onlineUrlsFetcher: ImageUrlsFetcher,
                    private val dbUrlsFetcher: DbUrlsFetcher,
                    private val dbUrlsSaver: DbUrlsSaver) : Presenter {

  private val subscription = SerialSubscription()

  override fun onViewCreated() = getImageUrlsFromDb()

  override fun onViewDestroyed() = subscription.unsubscribe()

  override fun updateView() = getNewImageUrls()

  private fun getNewImageUrls() = onlineUrlsFetcher.execute().updateUi(true)

  private fun getImageUrlsFromDb() = dbUrlsFetcher.execute().updateUi()

  private fun Observable<Collection<ViewModel>>.updateUi(andSaveToDb: Boolean = false) {
    observeOn(AndroidSchedulers.mainThread())
        .doOnNext { if (andSaveToDb) dbUrlsSaver.execute(*it.toTypedArray()) }
        .subscribe {
          view.updateData(it)
          view.hideLoading()
        }.let { subscription.set(it) }
  }
}