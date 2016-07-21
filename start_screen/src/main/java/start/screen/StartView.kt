package start.screen

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android_s14.catwallpaper.R
import shared.BaseView
import shared.bindView
import shared.hide
import shared.show
import javax.inject.Inject

class StartView : BaseView<ViewModel>() {

  @Inject override lateinit var presenter: StartPresenter
  override val component by lazy { applicationComponent.plus(StartViewModule(this)) }

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val loadingSpinner by bindView<View>(R.id.loading_indicator)
  private val listView by bindView<RecyclerView>(R.id.recycler_view)
  @Inject lateinit var listAdapter: ListAdapter

  @Inject lateinit var optionsDelegate: StartScreenOptionsMenuDelegate

  override fun onCreate(savedInstanceState: Bundle?) {
    component.inject(this)
    setupView(savedInstanceState)
    window.exitTransition = null
    presenter.onViewCreated()
  }

  override fun onCreateOptionsMenu(menu: Menu?) = optionsDelegate.onCreateOptionsMenu(menu)

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    val result = optionsDelegate.onOptionsItemSelected(item)
    return if (result) result else super.onOptionsItemSelected(item)
  }

  private fun setupView(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.start_screen)
    setSupportActionBar(toolbar)
    setupListView()
  }

  private fun setupListView() = with(listView) {
    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    itemAnimator = DefaultItemAnimator()
    adapter = listAdapter
  }

  override fun showLoading() = loadingSpinner.show()
  override fun hideLoading() = loadingSpinner.hide()
  override fun updateData(newData: Collection<ViewModel>) = listAdapter.swapData(newData)

}