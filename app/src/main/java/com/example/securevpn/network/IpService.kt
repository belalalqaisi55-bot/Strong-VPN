package com.example.securevpn.network

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class IpResponse(val ip: String)

interface IpService {
    @GET("?format=json")
    suspend fun getPublicIp(): IpResponse
}

object NetworkClient {
    private const val BASE_URL = "https://api.ipify.org/"
    
    val api: IpService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IpService::class.java)
    }
}
