package shared

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rx.Subscription
import kotlin.reflect.KClass
import java.lang.Enum

fun Subscription?.unsubscribeIfNeeded() {
  if (this != null && isUnsubscribed.not()) unsubscribe()
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attach: Boolean = false): View {
  return LayoutInflater.from(this.context).inflate(layoutId, this, attach)!!
}

fun View?.show() {
  if (this != null) {
    this.visibility = View.VISIBLE
  }
}

fun View?.hide() {
  if (this != null) {
    this.visibility = View.GONE
  }
}

inline fun <reified T : kotlin.Enum<T>> KClass<T>.safeValueOf(name: String): T? {
  try {
    return Enum.valueOf(T::class.java, name)
  } catch(ignored: Throwable) {
    return null
  }
}

fun <T, R> Pair<T, R>.toAndroidPair() = android.support.v4.util.Pair.create(first, second)