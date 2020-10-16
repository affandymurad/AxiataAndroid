package am.movie.axiata.news

import am.movie.axiata.Article
import am.movie.axiata.R
import am.movie.axiata.Response
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity(), NewsContract.View{

    private var mPresenter: NewsContract.Presenter? = null
    private val adapter by lazy { NewsAdapter() }
    private var newsList = ArrayList<Article>()
    private var isFinish = false

    var isSearch = false
    var search: SearchView?= null

    private var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setPresenter(NewsPresenter(this))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = category

        val layoutManager = LinearLayoutManager(this)
        rvArticle.layoutManager = layoutManager
        rvArticle.setHasFixedSize(true)
        rvArticle.adapter = adapter
        adapter.setOnItemClickListener(object: NewsAdapter.OnItemArticleClickListener{
            override fun onItemClick(article: Article) {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(this@NewsActivity, R.color.colorPrimary))
                builder.addDefaultShareMenuItem()
                builder.setShowTitle(true)
                builder.setStartAnimations(this@NewsActivity, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
                builder.setExitAnimations(this@NewsActivity, android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right)

                val packageName = builder.build().intent.setPackage("com.android.chrome")
                if (packageName == null) {
                    showError(getString(R.string.chrome))
                }
                else {
                    builder.build().launchUrl(this@NewsActivity, Uri.parse(article.url))
                }
            }

        })

        srArticle.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))

        srArticle.setOnRefreshListener {
            if (newsList.size != 0) {
                cleanRefreshItem()
            } else {
                page = 1
                mPresenter?.requestNewsList("ID", category, page)
            }
        }

        rvArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    page++
                    if (!isFinish) mPresenter?.requestNewsList("ID",category, page)
                }
            }
        })

        page = 1
        mPresenter?.requestNewsList("ID", category, page)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val itemSearch = menu?.findItem(R.id.mnSearch)

        itemSearch?.isVisible = isSearch

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.mnSearch)?.actionView as SearchView
        searchView.run {
            imeOptions = 0
            queryHint = getString(R.string.source_or_title)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isBlank()) {
                        adapter.newsList = newsList
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter.filterTitle(newText, newsList)
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    return true
                }
            })
        }
        search = searchView
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cleanRefreshItem() {
        newsList.clear()
        adapter.newsList = newsList
        adapter.notifyDataSetChanged()
    }

    private fun clearSearch() {
        isSearch = false
        search?.isIconified = true
        search?.isIconified = true
        search?.clearFocus()
        invalidateOptionsMenu()
    }


    override fun showNews(response: Response) {
        isFinish = newsList.size == response.totalResults
        newsList.addAll(response.articles)
        adapter.newsList = newsList
        adapter.notifyDataSetChanged()

        if (isFinish && newsList.size == 0) {
            rvArticle.visibility = View.GONE
            tvError.visibility = View.VISIBLE
        } else {
            isSearch = true
            invalidateOptionsMenu()
            rvArticle?.visibility = View.VISIBLE
            tvError?.visibility = View.GONE
        }
    }

    override fun showError(err: String) {
        srArticle.isRefreshing = false
        val snack = Snackbar.make(clArticle, err, Snackbar.LENGTH_SHORT)
        val text = snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        text.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snack.show()
    }

    override fun showLoading() {
        srArticle.isRefreshing = true
        clearSearch()
    }

    override fun dismissLoading() {
        srArticle.isRefreshing = false
    }

    override fun setPresenter(presenter: NewsContract.Presenter) {
        mPresenter = presenter
    }


    companion object {
        var category = ""
        var country = ""
        fun start(context: Context, country: String, category: String) {
            val starter = Intent(context, NewsActivity::class.java)
            this.category = category
            this.country = country
            context.startActivity(starter)
        }
    }
}