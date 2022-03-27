package com.example.manageincidents.presentaion.di.modules

import com.example.manageincidents.BuildConfig
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.presentaion.utils.AuthHeaderInterceptor
import com.example.manageincidents.presentaion.utils.CookieInterceptor
import com.example.manageincidents.presentaion.utils.LocaleUtil
import com.example.manageincidents.presentaion.utils.Session
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun authHeaderInterceptor(session: Session?): AuthHeaderInterceptor {
        return AuthHeaderInterceptor(session!!)
    }

    @Provides
    @Singleton
    fun provideInterceptor(localeUtil: LocaleUtil): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder().addHeader("lang", localeUtil.locale)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent","android").build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @Suppress("unused")
    fun provideCookieInterceptor(session: Session): CookieInterceptor {
        return CookieInterceptor(session)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor?,
        languageInterceptor: Interceptor?,
        authHeaderInterceptor: AuthHeaderInterceptor?
       // cookieInterceptor: CookieInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor!!)
            .addInterceptor(languageInterceptor!!)
            .addInterceptor(authHeaderInterceptor!!)
            //.cookieJar(cookieInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideErrorResponseBodyConverter(retrofit: Retrofit): Converter<ResponseBody, ApiErrorResponse> {
        return retrofit.responseBodyConverter(ApiErrorResponse::class.java, arrayOfNulls(0))
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(provideBaseUrl())
            .build()
    }


}