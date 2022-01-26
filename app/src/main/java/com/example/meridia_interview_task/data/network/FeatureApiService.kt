package com.example.meridia_interview_task.data.network

import com.example.meridia_interview_task.data.entity.FeatureResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FeatureApiService {

    @GET(".json")
    fun getFeatures(): Deferred<FeatureResponse>

    companion object{
        operator fun invoke(
        ): FeatureApiService {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://releases-f89f5.firebaseio.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FeatureApiService::class.java)
        }
    }
}
