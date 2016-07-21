package start.screen

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import shared.*
import javax.inject.Qualifier

@PerActivity
@Subcomponent(modules = arrayOf(StartViewModule::class, StartViewBinder::class))
interface StartComponent : Component {
  fun inject(target: StartView)
}

@Module
class StartViewModule(private val startView: StartView) {

  @Provides @PerActivity fun view() = startView

}

@Module
abstract class StartViewBinder() {

  @Binds
  abstract fun view(view: StartView): View<ViewModel>

  @Binds @RemoteUrlsFetcher
  abstract fun remoteRepository(imageUrlsFetcher: ImageUrlsFetcher): Interactor<Nothing, Collection<ViewModel>>

  @Binds @LocalUrlsFetcher
  abstract fun localRepository(dbUrlsFetcher: DbUrlsFetcher): Interactor<Nothing, Collection<ViewModel>>

  @Binds @UrlsSaver
  abstract fun localSavingRepository(dbUrlsSaver: DbUrlsSaver): Interactor<ViewModel, Boolean>

  @Binds
  abstract fun presenter(startPresenter: StartPresenter): Presenter

  @Binds
  abstract fun optionsDelegate(startScreenOptionsMenuDelegate: StartScreenOptionsMenuDelegate): OptionsMenuDelegate
}

@Qualifier annotation class RemoteUrlsFetcher
@Qualifier annotation class LocalUrlsFetcher
@Qualifier annotation class UrlsSaver