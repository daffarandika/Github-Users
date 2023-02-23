package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.adapter.SectionsPageAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.GithubUserDetail
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.networking.ApiService
import com.example.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val  TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        private const val TAG = "DetailActivity"
    }

    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val user = intent.extras!!.getString("username")

        val client = ApiConfig.getApiService().getUserDetail(user!!)
        client.enqueue(object: Callback<GithubUserDetail>{
            override fun onResponse(
                call: Call<GithubUserDetail>,
                response: Response<GithubUserDetail>,
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        detailViewModel.setGithubUser(responseBody)
                        setHeaderValue(responseBody)
                    }
                    else Log.e(TAG, "onResponse: ${response.message()}")
                } else Log.e(TAG, "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<GithubUserDetail>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })

        val sectionsPageAdapter = SectionsPageAdapter(this)
        val viewPager = findViewById<ViewPager2>(R.id.vp2)
        viewPager.adapter = sectionsPageAdapter
        val tabs = findViewById<TabLayout>(R.id.tl)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.hide()
    }

    private fun setHeaderValue(user: GithubUserDetail) {
        binding.tvUsername.text = user.login
        binding.tvName.text = user.name
        binding.tvBio.text = user.bio
        Glide.with(this@DetailActivity)
            .load(user.avatarUrl)
            .into(binding.ivUser)
    }
}