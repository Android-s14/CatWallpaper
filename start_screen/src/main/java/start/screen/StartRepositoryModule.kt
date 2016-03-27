package start.screen

import dagger.Module
import dagger.Provides
import shared.PerActivity
import shared.Repository
import shared.View
import javax.inject.Qualifier

@Module
class StartRepositoryModule {

  @Provides @PerActivity @OnlineRepository
  fun repository(view: View<ViewModel>): Repository<Nothing, Collection<ViewModel>> = CatsRepository(
      view)

  @Provides @PerActivity @DbRepository
  fun dbRepository(): Repository<Nothing, Collection<ViewModel>> = RealmRepository()

  @Provides @PerActivity @DbSavingRepository
  fun dbSavingRepository(): Repository<ViewModel, Boolean> = RealmSavingRepository()

  @Qualifier annotation class OnlineRepository
  @Qualifier annotation class DbRepository
  @Qualifier annotation class DbSavingRepository

}