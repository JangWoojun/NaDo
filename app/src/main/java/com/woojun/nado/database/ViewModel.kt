package com.woojun.nado.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.woojun.nado.Lecture
import com.woojun.nado.data.OnlineCourseList
import com.woojun.nado.network.RetrofitAPI
import com.woojun.nado.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {
    private val _onlineCourse = MutableLiveData<MutableList<Lecture>>()
    private val onlineCourse: LiveData<MutableList<Lecture>> = _onlineCourse

    private val _recommendationOnlineCourse = MutableLiveData<MutableList<Lecture>>()
    private val recommendationOnlineCourse: LiveData<MutableList<Lecture>> = _recommendationOnlineCourse
    fun loadOnlineCourse(callback: () -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<OnlineCourseList> = retrofitAPI.getOnlineCourseList()

        call.enqueue(object : Callback<OnlineCourseList> {
            override fun onResponse(call: Call<OnlineCourseList>, response: Response<OnlineCourseList>) {
                if (response.isSuccessful) {
                    _onlineCourse.value = response.body()?.map {
                        Lecture(
                            it.COURSE_NM,
                            it.CATEGORY_NM2,
                            "https://sll.seoul.go.kr${it.COURSE_IMAGE_FILE_PATH}",
                            it.link,
                            it.POPULARITY_YN == "N",
                            it.category
                        )
                    }?.toMutableList()
                }
            }

            override fun onFailure(call: Call<OnlineCourseList>, t: Throwable) {
                callback()
            }
        })
    }

    fun loadRecommendationOnlineCourseList(count: Int, callback: () -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<OnlineCourseList> = retrofitAPI.getRecommendationOnlineCourseList(count.toString())

        call.enqueue(object : Callback<OnlineCourseList> {
            override fun onResponse(call: Call<OnlineCourseList>, response: Response<OnlineCourseList>) {
                if (response.isSuccessful) {
                    _recommendationOnlineCourse.value = response.body()?.map {
                        Lecture(
                            it.COURSE_NM,
                            it.CATEGORY_NM2,
                            "https://sll.seoul.go.kr${it.COURSE_IMAGE_FILE_PATH}",
                            it.link,
                            it.POPULARITY_YN == "N",
                            it.category
                        )
                    }?.toMutableList()
                }
            }

            override fun onFailure(call: Call<OnlineCourseList>, t: Throwable) {
                callback()
            }
        })
    }

    fun getOnlineCourseList(): LiveData<MutableList<Lecture>> {
        return onlineCourse
    }

    fun getRecommendationOnlineCourseList(): LiveData<MutableList<Lecture>> {
        return recommendationOnlineCourse
    }
}
