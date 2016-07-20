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

@PerApplication
@dagger.Component(modules = arrayOf(
    NetworkModule::class,
    PicassoModule::class
))
interface ApplicationComponent : Component {

  fun plus(viewModule: ViewModule): StartComponent

  fun plus(viewModule: ExpandedViewModule) : ExpandedComponent

  fun inject(target: ImageHolder)

  fun inject(target: CatsRepository)

}

@Module
class NetworkModule {

  @Provides
  @PerApplication
  fun httpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    return OkHttpClient.Builder().addInterceptor(logging).build()
  }

  @Provides
  @PerApplication
  fun catsApi(client: OkHttpClient) = Retrofit.Builder()
      .baseUrl(Const.CATS_BASE_URL)
      .client(client)
      .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
      .build()
      .create(CatsApiInterface::class.java)!!
}

@Module
class PicassoModule(private val context: Context) {

  @Provides
  @PerApplication
  fun picasso() = Picasso.Builder(context)
      .indicatorsEnabled(true)
      .listener { picasso, uri, e ->
        Log.e("picasso", "error loading image", e)
      }
      .build()!!

}