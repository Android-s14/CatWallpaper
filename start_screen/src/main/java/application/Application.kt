package application

import android.app.Application
import android.content.Context
import android.util.Log
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import expanded.screen.ExpandedComponent
import expanded.screen.ExpandedViewModule
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import shared.Component
import shared.PerApplication
import start.screen.*

class Application : Application() {

  val component: ApplicationComponent by lazy {
    DaggerApplicationComponent.builder().picassoModule(PicassoModule(applicationContext)).build()
  }

  override fun onCreate() {
    super.onCreate()
    setupDefaultRealm()
  }

  private fun setupDefaultRealm() {
    val config = RealmConfiguration.Builder(this).build()
    Realm.setDefaultConfiguration(config)
  }
}