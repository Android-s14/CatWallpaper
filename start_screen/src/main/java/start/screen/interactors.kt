package start.screen

import shared.Interactor
import shared.PerActivity
import javax.inject.Inject

@PerActivity
class ImageUrlsFetcher
@Inject constructor(private val repository: CatsRepository) : Interactor<Nothing, Collection<ViewModel>> {

  override fun execute(vararg input: Nothing) = repository.execute()

}

@PerActivity
class DbUrlsFetcher
@Inject constructor(private val repository: RealmRepository) : Interactor<Nothing, Collection<ViewModel>> {

  override fun execute(vararg input: Nothing) = repository.execute()

}

@PerActivity
class DbUrlsSaver
@Inject constructor(private val repository: RealmSavingRepository) : Interactor<ViewModel, Boolean> {

  override fun execute(vararg input: ViewModel) = repository.execute(*input)

}