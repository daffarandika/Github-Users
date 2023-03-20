package com.example.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.preferences.SettingPreference

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private var pref: SettingPreference) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw java.lang.IllegalArgumentException("Unknown view model class $modelClass")
    }
}
fun <T: ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}
