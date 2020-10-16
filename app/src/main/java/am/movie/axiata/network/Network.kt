package am.movie.axiata.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Networks {

    val okhttpBuilder = OkHttpClient().newBuilder()
    val interceptor = HttpLoggingInterceptor()
    var retrofit: Retrofit? = null

    init {
        setup()
    }

    fun setup() {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val builder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS)

        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okhttpBuilder.addInterceptor(interceptor)

        retrofit = builder
            .baseUrl(urlNewsApi)
            .client(okhttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    companion object {

        fun service() : API{
            return Networks().retrofit!!.create(API::class.java)
        }

        const val urlNewsApi = "https://newsapi.org/v2/"
        const val newsList = "top-headlines"


    }
}
