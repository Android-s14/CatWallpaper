package start.screen

import android.app.Activity
import android.content.Intent
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android_s14.catwallpaper.R
import com.squareup.picasso.Picasso
import expanded.screen.ExpandedView
import shared.PerActivity
import shared.bindView
import shared.inflate
import javax.inject.Inject

@PerActivity
class ListAdapter @Inject constructor() : RecyclerView.Adapter<ImageHolder>() {

  private val data = mutableListOf<ViewModel>()
  @Inject lateinit var factory: HoldersFactory

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

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = factory.create(parent, R.layout.image_list_item)
}

@PerActivity
class HoldersFactory @Inject constructor(private val picasso: Picasso, private val activity: Activity) {
  fun create(parent: ViewGroup, @LayoutRes itemLayoutId: Int)
      = ImageHolder(parent.inflate(itemLayoutId), picasso, activity)
}

class ImageHolder(root: View, private val picasso: Picasso, private val activity: Activity)
: RecyclerView.ViewHolder(root) {

  val imageView: ImageView by bindView(R.id.image_view)

  lateinit var model: ViewModel

  fun loadImage() {
    picasso.load(model.imageUrl).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(imageView)
    setupClickBehaviour()
  }

  private fun setupClickBehaviour() {
    imageView.setOnClickListener { setupAndLaunchSharedTransition(it) }
  }

  private fun setupAndLaunchSharedTransition(imageView: View) {
    val sharedName = imageView.context.getString(R.string.expanded_view_transition_element)
    imageView.transitionName = sharedName
    val intent = Intent(imageView.context, ExpandedView::class.java).apply {
      putExtra(Const.INTENT_EXTRA_IMAGE_URL, model.imageUrl)
    }
    val options = makeSceneTransitionAnimation(activity, Pair.create(imageView, sharedName))
    imageView.context.startActivity(intent, options.toBundle())
  }

}