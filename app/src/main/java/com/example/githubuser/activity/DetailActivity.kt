package com.example.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPageAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.GithubUserDetail
import com.example.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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

        val username = intent.extras!!.getString("username")

        detailViewModel.getDetail(username!!)

        detailViewModel.githubUser.observe(this){ user ->
            setHeaderValue(user)
        }
        detailViewModel.isLoading.observe(this){isLoading ->
            showLoading(isLoading)
        }

        val sectionsPageAdapter = SectionsPageAdapter(this, username)
        val viewPager = findViewById<ViewPager2>(R.id.vp2)
        viewPager.adapter = sectionsPageAdapter
        val tabs = findViewById<TabLayout>(R.id.tl)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.hide()
    }

    private fun showLoading(loading: Boolean) {
        binding.pbDetail.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun setHeaderValue(user: GithubUserDetail) {
        binding.tvUsername.text = user.login
        binding.tvName.text = user.name
        binding.tvBio.text = user.bio
        if (user.bio.isNullOrEmpty()) {
            binding.tvBio.visibility = View.GONE
        }
        binding.tvFollowers.text = "Followers: ${user.followers}"
        binding.tvFollowings.text = "Following: ${user.following}"
        Glide.with(this@DetailActivity)
            .load(user.avatarUrl)
            .into(binding.ivUser)
    }
}