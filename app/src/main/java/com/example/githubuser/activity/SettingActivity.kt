package com.example.githubuser.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.preferences.SettingPreference
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.ViewModelFactory
import com.example.githubuser.viewmodel.createFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val switch = findViewById<SwitchCompat>(R.id.switchCompat)

        val pref = SettingPreference.getInstance(dataStore)
        val factory = MainViewModel(pref).createFactory()
        val mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        mainViewModel.getThemeSetting().observe(this) {isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch.isChecked = false
            }
        }
        switch.setOnCheckedChangeListener { compoundButton, isDarkMode ->
            mainViewModel.saveThemeSetting(isDarkMode)
        }
    }
}