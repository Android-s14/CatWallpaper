package shared

import android.view.Menu
import android.view.MenuItem

interface OptionsMenuDelegate {

  fun onCreateOptionsMenu(menu: Menu?): Boolean

  fun onOptionsItemSelected(item: MenuItem?): Boolean

}