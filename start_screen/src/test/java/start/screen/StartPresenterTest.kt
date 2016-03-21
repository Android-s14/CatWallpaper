package start.screen

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import shared.Interactor
import shared.View

class StartPresenterTest {

  private lateinit var presenter: StartPresenter

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setUp() {
    presenter = StartPresenter(mock(View::class.java) as View<ViewModel>)
    presenter.imageLoader = mock(Interactor::class.java) as Interactor<Nothing, Collection<ViewModel>>
  }

  @Test
  fun onViewCreated() {
    presenter.onViewCreated()
    verify(presenter.imageLoader, times(1)).execute(any())
  }

  @Test
  fun onViewDestroyed() {

  }
}