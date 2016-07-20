package start.screen

import android.view.Menu
import android.view.MenuItem
import com.android_s14.catwallpaper.R
import shared.OptionsMenuDelegate
import shared.PerActivity
import javax.inject.Inject

@PerActivity
class StartScreenOptionsMenuDelegate
@Inject constructor(private val activity: StartView,
                    private val presenter: StartPresenter) : OptionsMenuDelegate {

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    activity.menuInflater.inflate(R.menu.start_screen_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.start_screen_refresh -> presenter.updateView()
    }
    return true
  }

}