package expanded.screen

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import shared.Component
import shared.PerActivity
import start.screen.Const
import javax.inject.Qualifier

@PerActivity
@Subcomponent(modules = arrayOf(ExpandedViewModule::class))
interface ExpandedComponent : Component {

  fun inject(target: ExpandedView)

}

@Module
class ExpandedViewModule(private val view: ExpandedView) {

  @Provides @PerActivity fun view() = view
  @Provides @PerActivity @ImageUrl fun imageUrl() = view.intent.getStringExtra(Const.INTENT_EXTRA_IMAGE_URL)!!

}

@Qualifier annotation class ImageUrl