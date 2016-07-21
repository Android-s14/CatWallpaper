package start.screen

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.SerialSubscription
import shared.Interactor
import shared.PerActivity
import shared.Presenter
import shared.View
import javax.inject.Inject

@PerActivity
class StartPresenter
@Inject constructor(private val view: View<ViewModel>,
                    @RemoteUrlsFetcher private val remoteFetcher: Interactor<Nothing, Collection<ViewModel>>,
                    @LocalUrlsFetcher private val localFetcher: Interactor<Nothing, Collection<ViewModel>>,
                    @UrlsSaver private val urlsSaver: Interactor<ViewModel, Boolean>)
: Presenter {

  private val subscription = SerialSubscription()

  override fun onViewCreated() = getImageUrlsFromDb()

  override fun onViewDestroyed() = subscription.unsubscribe()

  override fun updateView() = getNewImageUrls()

  private fun getNewImageUrls() = remoteFetcher.execute().updateUi(true)

  private fun getImageUrlsFromDb() = localFetcher.execute().updateUi()

  private fun Observable<Collection<ViewModel>>.updateUi(andSaveToDb: Boolean = false) {
    observeOn(AndroidSchedulers.mainThread())
        .doOnNext { if (andSaveToDb) urlsSaver.execute(*it.toTypedArray()) }
        .subscribe {
          view.updateData(it)
          view.hideLoading()
        }.let { subscription.set(it) }
  }
}