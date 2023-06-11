package com.C23PS480.Rubist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.C23PS480.Rubist.Login.LoginViewModel
import com.C23PS480.Rubist.Model.UserPreference
import com.C23PS480.Rubist.Register.RegisterViewModel
import com.C23PS480.Rubist.ViewModel.CommunityViewModel

class ViewModelFactory(private val pref: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)->{
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(CommunityViewModel::class.java)->{
                CommunityViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}