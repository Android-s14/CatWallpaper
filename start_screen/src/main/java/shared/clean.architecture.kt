package shared

import android.support.v7.app.AppCompatActivity
import application.Application
import application.ApplicationComponent
import rx.Observable

interface View<T> {
  val presenter: Presenter<T>
  val component: Component
  val applicationComponent: ApplicationComponent

  fun showLoading()
  fun hideLoading()
  fun updateData(newData: Collection<T>)
}

interface Presenter<T> {
  val view: View<T>

  fun onViewCreated()

  fun onViewDestroyed()

  fun updateView()

}

interface Interactor<in K, T> {
  fun execute(vararg input: K): Observable<T>
}

interface Repository<in K, T> {
  fun execute(vararg input: K): Observable<T>
}

abstract class BaseView<T> : AppCompatActivity(), View<T> {

  override val applicationComponent by lazy { (application as Application).component }

}