package am.movie.axiata

import com.google.gson.annotations.SerializedName

data class Response (
    @SerializedName("status")
    var status: String,

    @SerializedName("totalResults")
    var totalResults: Int,

    @SerializedName("articles")
    var articles: ArrayList<Article>
)

data class Article (
    @SerializedName("source")
    var source: Source,

    @SerializedName("author")
    var author: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("url")
    var url: String? = null,

    @SerializedName("urlToImage")
    var urlToImage: String? = null,

    @SerializedName("publishedAt")
    var publishedAt: String? = null,

    @SerializedName("content")
    var content: String? = null
)

data class Source (
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null
)