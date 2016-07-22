package expanded.screen

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import shared.Component
import shared.PerActivity
import shared.Presenter
import shared.View
import start.screen.Const
import javax.inject.Qualifier

@PerActivity
@Subcomponent(modules = arrayOf(ExpandedViewModule::class, ExpandedViewBinder::class))
interface ExpandedComponent : Component {

  fun inject(target: ExpandedView)

}

@Module
class ExpandedViewModule(private val view: ExpandedView) {

  @Provides @PerActivity fun view(): View<String> = view
  @Provides @PerActivity @TargetImageView fun targetImageView() = view.imageView
  @Provides @PerActivity @ImageUrl fun imageUrl() = view.intent.getStringExtra(Const.INTENT_EXTRA_IMAGE_URL)!!

}

@Module
abstract class ExpandedViewBinder {
  @Binds abstract fun presenter(expandedPresenter: ExpandedPresenter): Presenter
}

@Qualifier annotation class ImageUrl
@Qualifier annotation class TargetImageView