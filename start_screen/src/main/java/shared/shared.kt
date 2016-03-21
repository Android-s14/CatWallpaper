package shared

import android.app.Activity
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import rx.Subscription
import kotlin.reflect.KClass
import java.lang.Enum

fun Subscription?.unsubscribeIfNeeded() {
  if (this != null && isUnsubscribed.not()) unsubscribe()
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attach: Boolean = false) = LayoutInflater.from(this.context).inflate(layoutId, this, attach)

fun Activity.inflateMenu(menuResId: Int, targetMenu: Menu) = menuInflater.inflate(menuResId, targetMenu)

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

inline fun <reified T : View> View.findView(id: Int): T {
  return findViewById(id) as T
}

inline fun <reified T : View> View.findOptionalView(id: Int): T? {
  return findViewById(id) as T?
}

inline fun <reified T : View> Activity.findView(id: Int): T {
  return findViewById(id) as T
}

inline fun <reified T : View> Activity.findOptionalView(id: Int): T? {
  return findViewById(id) as T?
}

inline fun <reified T : kotlin.Enum<T>> KClass<T>.safeValueOf(name: String): T? {
  try {
    return Enum.valueOf(T::class.java, name)
  } catch(ignored : Throwable) {
    return null
  }
}