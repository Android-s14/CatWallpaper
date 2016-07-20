package start.screen

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import shared.Component
import shared.PerActivity

@PerActivity
@Subcomponent(modules = arrayOf(StartViewModule::class))
interface StartComponent : Component {
  fun inject(target: StartView)
}

@Module
class StartViewModule(private val startView: StartView) {

  @Provides @PerActivity fun view() = startView

}