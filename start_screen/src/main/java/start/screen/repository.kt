package start.screen

import org.simpleframework.xml.Default
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import rx.schedulers.Schedulers
import shared.PerApplication
import shared.Repository
import javax.inject.Inject

@PerApplication
class CatsRepository
@Inject constructor(private val catsService: CatsApiInterface) : Repository<Nothing, Collection<ViewModel>> {

  override fun execute(vararg input: Nothing): Observable<Collection<ViewModel>> {
    return Observable.fromCallable { catsService.getImages().execute().body() }
        .subscribeOn(Schedulers.io())
        .flatMapIterable { it.data?.images }
        .map { ViewModel(it.id, it.url) }
        .toList()
        .map { it }
  }
}

interface CatsApiInterface {

  @GET(Const.IMAGES_URL)
  fun getImages(@Query(Const.API_KEY) apiKey: String? = null,
                @Query(Const.FORMAT) format: String = Const.FORMAT_XML,
                @Query(Const.RESULTS_PER_PAGE) results: Int = 5): Call<Response>

}

object Const {
  const val CATS_BASE_URL = "http://thecatapi.com"
  const val IMAGES_URL = "api/images/get"
  const val API_KEY = "api_key"
  const val FORMAT = "format"
  const val FORMAT_XML = "xml"
  const val RESULTS_PER_PAGE = "results_per_page"

  const val INTENT_EXTRA_IMAGE_URL = "INTENT_EXTRA_IMAGE_URL"
}

@Default data class Response(var data: Images? = null)
@Default data class Images(var images: List<Image>? = null)
@Default data class Image(var url: String = "", var id: String = "")