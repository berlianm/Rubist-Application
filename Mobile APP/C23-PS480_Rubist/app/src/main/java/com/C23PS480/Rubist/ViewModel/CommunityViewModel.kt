package com.C23PS480.Rubist.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.C23PS480.Rubist.Model.UserModel
import com.C23PS480.Rubist.Model.UserPreference

class CommunityViewModel (private val pref : UserPreference) : ViewModel() {
    fun getUser() : LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }
}