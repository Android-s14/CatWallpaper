package start.screen

import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable
import shared.BaseTest
import shared.Interactor
import shared.View

class StartPresenterTest : BaseTest() {

  private lateinit var presenter: StartPresenter

  private lateinit var view: View<ViewModel>
  private lateinit var remoteFetcher: Interactor<Nothing, Observable<Collection<ViewModel>>>
  private lateinit var localFetcher: Interactor<Nothing, Observable<Collection<ViewModel>>>
  private lateinit var localSaver: Interactor<ViewModel, Unit>

  override fun init() {
    view = mock()
    remoteFetcher = mock()
    localFetcher = mock()
    localSaver = mock()

    whenever(remoteFetcher.execute()).thenReturn(Observable.just(listOf(ViewModel("id", "url"))))
    whenever(localFetcher.execute()).thenReturn(Observable.just(listOf(ViewModel("id", "url"))))

    presenter = StartPresenter(view, remoteFetcher, localFetcher, localSaver)
  }

  @Test
  fun `test urls fetched from db on view created`() {
    presenter.onViewCreated()
    verify(localFetcher, times(1)).execute()
    verify(remoteFetcher, never()).execute()
    verify(localSaver, never()).execute()
  }

  @Test
  fun `test new urls fetched and saved on update view`() {
    presenter.updateView()
    verify(localFetcher, never()).execute()
    verify(remoteFetcher, times(1)).execute()
    verify(localSaver, times(1)).execute(ViewModel("id", "url"))
    verify(view, times(1)).hideLoading()
    verify(view, times(1)).updateData(anyCollection())
  }

  @Test
  fun `does not crash on empty collection from db`() {
    whenever(localFetcher.execute()).thenReturn(Observable.just(emptyList()))
    presenter.onViewCreated()
    verify(view, times(1)).hideLoading()
    verify(view, times(1)).updateData(emptyList())
  }

  @Test
  fun `does not crash on empty collection from api`() {
    whenever(remoteFetcher.execute()).thenReturn(Observable.just(emptyList()))
    presenter.updateView()
    verify(localSaver, times(1)).execute(*emptyArray<ViewModel>())
    verify(view, times(1)).hideLoading()
    verify(view, times(1)).updateData(emptyList())
  }

  @Test
  fun `does not crash on view destroyed`() {
    presenter.onViewDestroyed()
  }

}