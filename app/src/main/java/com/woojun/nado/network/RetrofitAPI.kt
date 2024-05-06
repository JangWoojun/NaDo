package com.woojun.nado.network

import com.woojun.nado.BuildConfig
import com.woojun.nado.data.OnlineCourseList
import com.woojun.nado.data.TbViewProgram
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitAPI {
    @GET("${BuildConfig.baseUrl}lecture/get/OnlineCoures")
    fun getOnlineCourseList(): Call<OnlineCourseList>

    @GET("${BuildConfig.baseUrl}lecture/get/tbViewProgram")
    fun getEducationList(): Call<TbViewProgram>

    @GET("${BuildConfig.baseUrl}lecture/get/OnlineCoures/{count}")
    fun getRecommendationOnlineCourseList(
        @Path("count") count: String
    ): Call<OnlineCourseList>
}