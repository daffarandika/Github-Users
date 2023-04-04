package com.example.githubuser.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPageAdapter
import com.example.githubuser.database.GithubUserDatabase
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.createFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val apiService = ApiConfig.getApiService()
        val dao = GithubUserDatabase.getInstance(this@DetailActivity).getDao()
        val detailViewModelFactory = DetailViewModel(GithubUserRepository.getInstance(apiService, dao)).createFactory()
        detailViewModel = ViewModelProvider(this, detailViewModelFactory)[DetailViewModel::class.java]
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            this.finish()
            overridePendingTransition(R.anim.slide_left, R.anim.slide_left)
        }

        val username = intent.extras!!.getString("username")

        detailViewModel.getUserDetail(username!!).observe(this){res ->
            when (res) {
                is com.example.githubuser.model.Result.Error -> {
                    binding.pbDetail.visibility = View.GONE
                    Toast.makeText(this@DetailActivity, res.message, Toast.LENGTH_SHORT).show()
                }
                is com.example.githubuser.model.Result.Loading -> {
                    binding.pbDetail.visibility = View.VISIBLE
                }
                is com.example.githubuser.model.Result.Success -> {
                    binding.pbDetail.visibility = View.GONE
                    val user = res.data
                    binding.tvUsername.text = user.login
                    binding.tvName.text = user.name
                    binding.tvBio.text = user.bio

                    if (user.bio.isNullOrEmpty()) {
                        binding.tvBio.visibility = View.GONE
                    }

                    lifecycleScope.launch {
                        if (detailViewModel.isUserFavorite(user.login)) {
                            binding.ibHeart.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_favorite_large))
                        } else {
                            binding.ibHeart.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_favorite_border_large))
                        }
                    }
                    binding.ibHeart.setOnClickListener {
                        lifecycleScope.launch {
                            if (detailViewModel.isUserFavorite(user.login)) {
                                detailViewModel.unsetUserAsFavorite(user.login)
                                binding.ibHeart.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_favorite_border_large))
                            } else {
                                detailViewModel.unsetUserAsFavorite(user.login)
                                binding.ibHeart.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_favorite_border_large))
                            }
                        }
                    }

                    val formatter = "%,d"
                    binding.tvFollowers.text = formatter.format(user.followers)
                    binding.tvFollowings.text = formatter.format(user.following)
                    Glide.with(this@DetailActivity)
                        .load(user.avatarUrl)
                        .into(binding.ivUser)

                    binding.ibHeart.setOnClickListener {
                        lifecycleScope.launch {
                            if (detailViewModel.isUserFavorite(user.login)) {
                                binding.ibHeart.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_favorite_border_large))
                                detailViewModel.unsetUserAsFavorite(user.login)
                                Toast.makeText(this@DetailActivity, "Removed user from favorites", Toast.LENGTH_SHORT).show()
                            } else {
                                binding.ibHeart.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_favorite_large))
                                detailViewModel.setUserAsFavorite(user.login)
                                Toast.makeText(this@DetailActivity, "Added user to favorites", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
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

    companion object {
        @StringRes
        private val  TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        private const val TAG = "DetailActivity"
    }

}