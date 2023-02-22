package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.model.*
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.viewmodel.SearchViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)
        val layoutManager = LinearLayoutManager(this)
        binding.rv.layoutManager = layoutManager

        searchViewModel.githubUsers.observe(this) { users ->
            binding.rv.adapter = GithubUserAdapter(users, this@MainActivity)
        }

        binding.svUsers.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                val githubUsers = mutableListOf<GithubUser>()
                val client = ApiConfig.getApiService().searchUser(query)
                client.enqueue(object: Callback<GithubSearchResponse>{

                    override fun onResponse(
                        call: Call<GithubSearchResponse>,
                        response: Response<GithubSearchResponse>,
                    ) {
                        Log.d(TAG, "onQueryTextSubmit: $response")
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val users = responseBody.items
                                for (user in users){
                                    githubUsers.add(user)
                                }
                                binding.rv.adapter = GithubUserAdapter(githubUsers, this@MainActivity)
                            } else {
                                Log.e(TAG, "onResponse: ${response.message()}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<GithubSearchResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                    }

                })
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }

        })
    }


    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

}