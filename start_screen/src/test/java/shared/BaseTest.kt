package shared

import org.junit.After
import org.junit.Before
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

abstract class BaseTest {

  @Before
  open fun setUp() {
    registerRxJavaHooks()
    init()
  }

  abstract fun init()

  @After
  fun tearDown() {
    resetRxJavaHooks()
  }

  private fun registerRxJavaHooks() {
    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
      override fun getMainThreadScheduler(): Scheduler {
        return Schedulers.immediate()
      }
    })
    RxJavaHooks.setOnIOScheduler { Schedulers.immediate() }
    RxJavaHooks.setOnComputationScheduler { Schedulers.immediate() }
  }

  private fun resetRxJavaHooks() {
    RxJavaHooks.reset()
    RxAndroidPlugins.getInstance().reset()
  }
}