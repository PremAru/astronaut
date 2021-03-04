package com.space.astronaut.interceptor

import android.content.Context
import com.space.astronaut.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ForceCacheInterceptor @Inject constructor(context: Context): Interceptor {
    val application = context

    override fun intercept(chain: Interceptor.Chain): Response {
        var builder = chain.request().newBuilder()
        if (!NetworkUtils.internetAvailable(application)){
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
    }
}