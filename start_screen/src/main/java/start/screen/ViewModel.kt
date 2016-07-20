package start.screen

data class ViewModel(val imageId: Long, val imageUrl: String) {
  constructor(imageId: String, imageUrl: String) : this(imageId.hashCode().toLong(), imageUrl)
}