package com.example.githubuser.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.preferences.SettingPreference
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.SearchViewModel
import com.example.githubuser.viewmodel.createFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var searchViewModel: SearchViewModel
    private var isUsingLinear = true

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            Intent(this@MainActivity, SettingActivity::class.java).apply {
                startActivity(this)
            }
        }
        return true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.rv.layoutManager = LinearLayoutManager(this)
        val searchViewModelFactory = SearchViewModel().createFactory()
        searchViewModel = ViewModelProvider(this, searchViewModelFactory)[SearchViewModel::class.java]
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(dataStore)
        val mainViewModelFactory = MainViewModel(pref).createFactory()
        val mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        mainViewModel.getThemeSetting().observe(this) {isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchViewModel.githubUsers.observe(this) {res ->
            when (res) {
                is com.example.githubuser.model.Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is com.example.githubuser.model.Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, res.message, Toast.LENGTH_SHORT).show()
                }
                is com.example.githubuser.model.Result.Success -> {
                    if (res.data.isEmpty()) {
                        Toast.makeText(this@MainActivity, "No users were found", Toast.LENGTH_SHORT).show()
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.rv.apply {
                        layoutManager = GridLayoutManager(this@MainActivity, 3)
                        adapter = GithubUserAdapter(res.data, this@MainActivity)
                    }
                }
            }
        }

        binding.svUsers.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.searchUser(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

    }

}