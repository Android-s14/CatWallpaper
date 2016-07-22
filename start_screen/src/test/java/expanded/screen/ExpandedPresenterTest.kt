package expanded.screen

import android.widget.ImageView
import com.nhaarman.mockito_kotlin.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.anyString
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import shared.BaseTest
import shared.View

@RunWith(MockitoJUnitRunner::class)
class ExpandedPresenterTest : BaseTest() {

  private lateinit var presenter: ExpandedPresenter
  @Mock private lateinit var view: View<String>
  @Mock lateinit var imageView: ImageView
  private lateinit var picasso: Picasso
  private val imageUrl = "url"

  override fun init() {
    picasso = mock()
    doReturn(mock<RequestCreator>()).whenever(picasso).load(anyString())
    presenter = ExpandedPresenter(view, imageView, imageUrl, picasso)
  }

  @Test
  fun `test image loading on view created`() {
    presenter.onViewCreated()
    verify(picasso, times(1)).load(eq(imageUrl))
  }

  @Test
  fun `test image loading on update view`() {
    presenter.updateView()
    verify(picasso, times(1)).load(eq(imageUrl))
  }

}