package shared

import android.support.v7.app.AppCompatActivity
import application.Application
import application.ApplicationComponent
import rx.Observable

interface View<in T> {
  val presenter: Presenter
  val component: Component
  val applicationComponent: ApplicationComponent

  fun showLoading()
  fun hideLoading()
  fun updateData(newData: Collection<T>)
}

interface Presenter {

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

abstract class BaseView<in T> : AppCompatActivity(), View<T> {

  override val applicationComponent by lazy { (application as Application).component }

}