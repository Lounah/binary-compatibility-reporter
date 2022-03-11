package com.lounah.ktbinaryreporter.api

import com.lounah.ktbinaryreporter.api.internal.Authenticators
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

public interface RetrofitProvider {

    public fun provide(username: String, password: String): Retrofit
    public fun provide(credentials: String): Retrofit

    public companion object {

        public operator fun invoke(url: String, logging: Boolean): RetrofitProvider {
            return Impl(url, logging)
        }
    }

    private class Impl(
        private val url: String,
        private val logging: Boolean
    ) : RetrofitProvider {

        override fun provide(username: String, password: String): Retrofit {
            return buildRetrofit(Authenticators.basic(username, password))
        }

        override fun provide(credentials: String): Retrofit {
            return buildRetrofit(Authenticators.base64(credentials))
        }

        private fun buildRetrofit(authenticator: Authenticator): Retrofit {
            val client = OkHttpClient.Builder()
                .authenticator(authenticator)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .also { if (logging) it.addInterceptor(HttpLoggingInterceptor()) }
                .build()

            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .build()
        }
    }
}