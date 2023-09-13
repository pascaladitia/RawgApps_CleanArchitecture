package com.pascal.rawgapps.di

import com.pascal.rawgapps.common.Constants
import com.pascal.rawgapps.data.remote.service.IRawgAPI
import com.pascal.rawgapps.data.repository.ApiRepositoryImpl
import com.pascal.rawgapps.domain.repository.IApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRawgApi(): IRawgAPI {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logger)

        return Retrofit.Builder()
            .baseUrl(Constants.RAWG_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(IRawgAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepository(api: IRawgAPI): IApiRepository {
        return ApiRepositoryImpl(api)
    }
}