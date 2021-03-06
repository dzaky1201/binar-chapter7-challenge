package com.dzakyhdr.moviedb.network

import com.dzakyhdr.moviedb.data.remote.model.popular.PopularResponse
import com.dzakyhdr.moviedb.data.remote.model.popular.Result
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET(EndPoint.Popular.urlPopular)
    suspend fun getPopular(
        @Query("api_key") api: String,
        @Query("page") page: Int = 1
    ): PopularResponse

    @GET(EndPoint.Detail.detail)
    suspend fun getDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") api: String
    ): Response<Result>

}



object EndPoint {

    object Popular {
        const val urlPopular = "movie/popular"
    }

    object Detail {
        const val detail = "movie/{movieId}"
    }
}