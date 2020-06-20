package com.example.flightmobileapp
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("/screenshot")
    fun getImg(): Call<ResponseBody>

    /*@POST("/command")
    fun postCommand(): Call
*/
}