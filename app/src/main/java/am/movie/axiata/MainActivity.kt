package am.movie.axiata

import am.movie.axiata.news.NewsActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val adapter by lazy { CategoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        rvCategory.layoutManager = layoutManager
        rvCategory.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(user: String) {
               NewsActivity.start(this@MainActivity, "ID", user)
            }
        })

    }
}