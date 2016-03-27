package start.screen

import dagger.Module
import dagger.Provides
import shared.Interactor
import shared.PerActivity
import shared.Repository
import shared.View
import javax.inject.Inject
import javax.inject.Qualifier

class ImageUrlsFetcher(view: View<ViewModel>) : Interactor<Nothing, Collection<ViewModel>> {

  @Inject @field:[StartRepositoryModule.OnlineRepository] lateinit var repository: Repository<Nothing, Collection<ViewModel>>

  init {
    (view.component as StartComponent).inject(this)
  }

  override fun execute(vararg input: Nothing) = repository.execute()
}

class DbUrlsFetcher(view: View<ViewModel>) : Interactor<Nothing, Collection<ViewModel>> {

  @Inject @field:[StartRepositoryModule.DbRepository] lateinit var repository: Repository<Nothing, Collection<ViewModel>>

  init {
    (view.component as StartComponent).inject(this)
  }

  override fun execute(vararg input: Nothing) = repository.execute()
}

class DbUrlsSaver(view: View<ViewModel>) : Interactor<ViewModel, Boolean> {
  @Inject @field:[StartRepositoryModule.DbSavingRepository] lateinit var repository: Repository<ViewModel, Boolean>

  init {
    (view.component as StartComponent).inject(this)
  }

  override fun execute(vararg input: ViewModel) = repository.execute(*input)
}

@Module
class InteractorModule {

  @Provides @PerActivity @OnlineUrlsFetcher
  fun interactor(view: View<ViewModel>): Interactor<Nothing, Collection<ViewModel>> = ImageUrlsFetcher(view)

  @Provides @PerActivity @DbUrlsFetcher
  fun dbInteractor(view: View<ViewModel>): Interactor<Nothing, Collection<ViewModel>> = DbUrlsFetcher(view)

  @Provides @PerActivity @DbUrlsSaver
  fun dbSaver(view: View<ViewModel>): Interactor<ViewModel, Boolean> = DbUrlsSaver(view)

  @Qualifier annotation class OnlineUrlsFetcher
  @Qualifier annotation class DbUrlsFetcher
  @Qualifier annotation class DbUrlsSaver
}