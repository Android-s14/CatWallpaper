package start.screen

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import rx.Observable
import rx.subjects.PublishSubject
import shared.PerApplication
import shared.Repository
import javax.inject.Inject

@PerApplication
class RealmRepository
@Inject constructor() : Repository<ViewModel> {

  override fun query(): Observable<Collection<ViewModel>> {
    val subject = PublishSubject.create<Collection<ViewModel>>()
    val results = Realm.getDefaultInstance().where(RealmViewModel::class.java).findAllAsync()
    results.addChangeListener {
      results.removeChangeListeners()
      val collection = results.map { ViewModel(it.imageId, it.imageUrl) }.toList()
      subject.onNext(collection)
    }
    return subject
  }

  override fun write(vararg items: ViewModel) {
    val realm = Realm.getDefaultInstance()
    realm.executeTransactionAsync({
                                    val localRealm = Realm.getDefaultInstance()
                                    items.forEach {
                                      val model = localRealm.createObject(RealmViewModel::class.java)
                                      model.imageId = it.imageId
                                      model.imageUrl = it.imageUrl
                                    }
                                  })
  }
}

open class RealmViewModel : RealmObject() {
  @field:[PrimaryKey] var imageId: Long = -1
  lateinit var imageUrl: String
}