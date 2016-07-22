package start.screen

import rx.Observable
import shared.Interactor
import shared.PerActivity
import javax.inject.Inject

@PerActivity
class ImageUrlsFetcher
@Inject constructor(private val repository: CatsRepository) : Interactor<Nothing, Observable<Collection<ViewModel>>> {

  override fun execute(vararg input: Nothing) = repository.query()

}

@PerActivity
class DbUrlsFetcher
@Inject constructor(private val repository: RealmRepository) : Interactor<Nothing, Observable<Collection<ViewModel>>> {

  override fun execute(vararg input: Nothing) = repository.query()

}

@PerActivity
class DbUrlsSaver
@Inject constructor(private val repository: RealmRepository) : Interactor<ViewModel, Unit> {

  override fun execute(vararg input: ViewModel) = repository.write(*input)

}