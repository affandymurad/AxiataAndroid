package am.movie.axiata.news

import am.movie.axiata.Response
import am.movie.axiata.network.Networks
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class NewsPresenter(private val mView: NewsContract.View): NewsContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    private val composite = CompositeDisposable()

    override fun requestNewsList(country: String, category: String, page :Int) {
        mView.showLoading()
        Networks.service().getNewsList("14be9184c0b940aeba41747588d3beba",country, category, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response> {
                override fun onSubscribe(d: Disposable) {
                    composite.add(d)
                }

                override fun onNext(result: Response) {
                    mView.showNews(result)
                }

                override fun onError(e: Throwable) {
                    mView.dismissLoading()
                    mView.showError(e.localizedMessage ?: "Unknown")
                }

                override fun onComplete() {
                    mView.dismissLoading()
                }
            })
    }

}