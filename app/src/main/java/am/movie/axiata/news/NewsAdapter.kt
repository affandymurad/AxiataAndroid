package am.movie.axiata.news

import am.movie.axiata.Article
import am.movie.axiata.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_article.view.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var newsList = ArrayList<Article>()

    private var mOnItemArticleClickListener: OnItemArticleClickListener? = null

    fun setOnItemClickListener(onItemArticleClickListener: OnItemArticleClickListener) {
        mOnItemArticleClickListener = onItemArticleClickListener
    }

    fun filterTitle(title: String, fullList : ArrayList<Article>) {
        val temp = java.util.ArrayList<Article>()
        fullList.filterTo(temp) {
            it.source.name!!.toLowerCase().contains(title.toLowerCase()) || it.title!!.toLowerCase().contains(title.toLowerCase())
        }
        newsList = temp
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }


    inner class ViewHolder (view: View) :RecyclerView.ViewHolder(view) {

        private val llArticle = view.llArticle
        private val tvArticle = view.tvArticle
        private val tvDate = view.tvDate
        private val ivArticle = view.ivArticle
        private val tvSource = view.tvSource

        fun bind (article: Article) {

            val indonesia = Locale("in","ID","ID")
            val df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, indonesia)


            tvArticle.text = article.title
            tvDate.text = df.format(article.publishedAt?.getDateWithServerTimeStamp())
            tvSource.text = article.source.name
            Glide.with(itemView.context).load(article.urlToImage).apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder)).into(ivArticle)

            if (mOnItemArticleClickListener != null) {
                llArticle.setOnClickListener {
                    mOnItemArticleClickListener?.onItemClick(article)
                }
            }
        }
    }

    fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
        return try {
            dateFormat.parse(this)
        } catch (e: ParseException) {
            null
        }

}

interface OnItemArticleClickListener{
    fun onItemClick(article: Article)
}}