package am.movie.axiata.news

import am.movie.axiata.Article
import am.movie.axiata.Response
import am.movie.axiata.base.BaseView

interface NewsContract {
    interface View : BaseView<Presenter> {

        fun showNews(response: Response)

        fun showError(err: String)

        fun showLoading()

        fun dismissLoading()
    }

    interface Presenter {
        fun requestNewsList(country: String, category: String, page :Int)
    }
}