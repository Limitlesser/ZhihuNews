package wind.zhihunews.model


data class Story(val id: Int, val type: Int, val title: String, val image: String?) {
    var images: List<String>? = null
    val firstImage
        get() = image ?: images?.get(0)
}

data class StoryDetail(val id: Int, val body: String, val title: String, val image: String,
                       val image_source: String, val share_url: String)

data class TopStory(val id: Int, val type: Int, val title: String, val image: String, val ga_prefix: String)

data class News(val date: String? = null, val stories: List<Story>, val top_stories: List<TopStory>?)

data class StartImage(var text: String, var img: String)
