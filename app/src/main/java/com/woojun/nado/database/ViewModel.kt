package com.woojun.nado.database

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.nado.adapter.EducationAdapter
import com.woojun.nado.data.Education
import com.woojun.nado.data.Lecture
import com.woojun.nado.data.OnlineCourseList
import com.woojun.nado.data.TbViewProgram
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

    private val _educationList = MutableLiveData<MutableList<Education>>()
    private val educationList: LiveData<MutableList<Education>> = _educationList

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

    fun loadEducationList(callback: () -> Unit) {
        val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
        val call: Call<TbViewProgram> = retrofitAPI.getEducationList()

        call.enqueue(object : Callback<TbViewProgram> {
            override fun onResponse(
                call: Call<TbViewProgram>,
                response: Response<TbViewProgram>
            ) {
                if (response.isSuccessful) {
                    _educationList.value = response.body()?.map {
                        Education(it.SUBJECT, "${it.APPLICATIONSTARTDATE} ~ ${it.APPLICATIONENDDATE}", it.APPLY_STATE, it.VIEWDETAIL)
                    }?.toMutableList()
                }
            }

            override fun onFailure(call: Call<TbViewProgram>, t: Throwable) {
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

    fun getEducationList(): LiveData<MutableList<Education>> {
        return educationList
    }
}
