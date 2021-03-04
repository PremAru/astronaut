package com.space.astronaut.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.space.astronaut.interceptor.ForceCacheInterceptor
import com.space.astronaut.service.AstronautService
import com.space.astronaut.utils.Constants.BASEURL
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module()
class NetworkModule {

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun providesCache(context: Context): Cache {
        var cacheSize: Long = 10 * 1024 * 1024 //10 MB
        var httpCacheDirectory: File = File(context.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache,
        forceCacheInterceptor: ForceCacheInterceptor
    ): OkHttpClient {

        var logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        var httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(forceCacheInterceptor)
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }


    @Singleton
    @Provides
    fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRepositoryApiService(retrofit: Retrofit): AstronautService {
        return retrofit.create(AstronautService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttp3Downloader(okHttpClient: OkHttpClient) : OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @Singleton
    @Provides
    fun providePicasso(context: Context, okHttp3Downloader: OkHttp3Downloader) : Picasso {
        return Picasso.Builder(context)
            .downloader(okHttp3Downloader)
            .build()
    }
}