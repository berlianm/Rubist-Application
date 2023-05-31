package com.cp23ps480.rubist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cp23ps480.rubist.Model.UserModel
import com.cp23ps480.rubist.Model.UserPreference
import com.cp23ps480.rubist.API.Response.ListStory
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel(){
    private val _listStory = MutableLiveData<List<ListStory>>()
    val listStory: LiveData<List<ListStory>> = _listStory

    private var _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

//    private val _locationStory = MutableLiveData<List<ListStory>>()
//    val locationStory: LiveData<List<ListStory>> = _locationStory

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

//    fun getStory(token: String) {
//        ApiConfig.getApiService()
//            .getStory("Bearer $token")
//            .enqueue(object : Callback<StoryResponse> {
//                override fun onResponse(
//                    call: Call<StoryResponse>,
//                    response: Response<StoryResponse>
//                ) {
//                    if (response.isSuccessful){
//                        val responseBody = response.body()
//                        if (responseBody != null) {
//                            _listStory.value = responseBody.listStory
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                    Log.d("Faillure" , t.message.toString())
//                }
//
//            })
//    }


}