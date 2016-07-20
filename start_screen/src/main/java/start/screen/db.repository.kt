package start.screen

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import rx.Observable
import rx.subjects.PublishSubject
import shared.Repository

class RealmRepository() : Repository<Nothing, Collection<ViewModel>> {

  override fun execute(vararg input: Nothing): Observable<Collection<ViewModel>> {
    val subject = PublishSubject.create<Collection<ViewModel>>()
    val results = Realm.getDefaultInstance().where(RealmViewModel::class.java).findAllAsync()
    results.addChangeListener {
      results.removeChangeListeners()
      val collection = results.map { ViewModel(it.imageId, it.imageUrl) }.toList()
      subject.onNext(collection)
    }
    return subject
  }
}

class RealmSavingRepository() : Repository<ViewModel, Boolean> {

  override fun execute(vararg input: ViewModel): Observable<Boolean> {
    val result = PublishSubject.create<Boolean>()
    val realm = Realm.getDefaultInstance()
    realm.executeTransactionAsync({
                                    val localRealm = Realm.getDefaultInstance()
                                    input.forEach {
                                      val model = localRealm.createObject(RealmViewModel::class.java)
                                      model.imageId = it.imageId
                                      model.imageUrl = it.imageUrl
                                    }
                                  },
                                  { result.onNext(true); result.onCompleted() },
                                  { result.onNext(false); result.onCompleted() })
    return result
  }
}

open class RealmViewModel : RealmObject() {
  @field:[PrimaryKey] var imageId: Long = -1
  lateinit var imageUrl: String
}