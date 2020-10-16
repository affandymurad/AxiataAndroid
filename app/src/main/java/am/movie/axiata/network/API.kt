package am.movie.axiata.network


import am.movie.axiata.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET(Networks.newsList)
    fun getNewsList(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("page") page: Int
    ) : Observable<Response>

}