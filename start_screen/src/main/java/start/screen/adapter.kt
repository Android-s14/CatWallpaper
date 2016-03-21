package start.screen

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.android_s14.catwallpaper.R
import com.squareup.picasso.Picasso
import expanded.screen.ExpandedView
import shared.inflate
import shared.toAndroidPair
import javax.inject.Inject

class ImageHolder(root: View, view: shared.View<ViewModel>) : RecyclerView.ViewHolder(root) {

  val imageView: ImageView by bindView(R.id.image_view)
  lateinit var model: ViewModel

  @Inject lateinit var picasso: Picasso

  init {
    view.applicationComponent.inject(this)
  }

  fun loadImage() {
    checkNotNull(model, { "forgot to assign model to image holder " })
    picasso.load(model.imageUrl).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(imageView)
    setupClickBehaviour()
  }

  private fun setupClickBehaviour() {
    imageView.setOnClickListener { setupAndLaunchSharedTransition(it) }
  }

  private fun setupAndLaunchSharedTransition(it: View) {
    val sharedName = it.context.getString(R.string.expanded_view_transition_element)
    it.transitionName = sharedName
    val intent = Intent(it.context, ExpandedView::class.java).apply { putExtra(Const.INTENT_EXTRA_IMAGE_URL, model.imageUrl) }
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, (it to sharedName).toAndroidPair())
    it.context.startActivity(intent, options.toBundle())
  }
}

class ListAdapter(private val view: shared.View<ViewModel>) : RecyclerView.Adapter<ImageHolder>() {

  init {
    setHasStableIds(true)
  }

  val data = mutableListOf<ViewModel>()

  fun swapData(newItem: Collection<ViewModel>) {
    data.clear()
    data.addAll(newItem)
    notifyDataSetChanged()
  }

  override fun getItemCount() = data.size

  override fun getItemId(position: Int) = data[position].imageId

  override fun onBindViewHolder(holder: ImageHolder?, position: Int) {
    holder?.apply { model = data[position] }?.loadImage()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder? {
    return ImageHolder(parent.inflate(R.layout.image_list_item), view)
  }
}