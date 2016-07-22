package start.screen

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import rx.Observable
import shared.*
import javax.inject.Qualifier

@PerActivity
@Subcomponent(modules = arrayOf(StartViewModule::class, StartViewBinder::class))
interface StartComponent : Component {
  fun inject(target: StartView)
}

@Module
class StartViewModule(private val startView: StartView) {

  @Provides @PerActivity fun view(): View<ViewModel> = startView
  @Provides @PerActivity fun activity(): Activity = startView

}

@Module
abstract class StartViewBinder() {

  @Binds @RemoteUrlsFetcher
  abstract fun remoteFetcher(imageUrlsFetcher: ImageUrlsFetcher): Interactor<Nothing, Observable<Collection<ViewModel>>>

  @Binds @LocalUrlsFetcher
  abstract fun localFetcher(dbUrlsFetcher: DbUrlsFetcher): Interactor<Nothing, Observable<Collection<ViewModel>>>

  @Binds @UrlsSaver
  abstract fun localSaver(dbUrlsSaver: DbUrlsSaver): Interactor<ViewModel, Unit>

  @Binds
  abstract fun presenter(startPresenter: StartPresenter): Presenter

  @Binds
  abstract fun optionsDelegate(startScreenOptionsMenuDelegate: StartScreenOptionsMenuDelegate): OptionsMenuDelegate
}

@Qualifier annotation class RemoteUrlsFetcher
@Qualifier annotation class LocalUrlsFetcher
@Qualifier annotation class UrlsSaver