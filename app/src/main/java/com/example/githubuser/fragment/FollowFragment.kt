package com.example.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.model.GithubUser
import com.example.githubuser.model.local.GithubUserEntity
import com.example.githubuser.viewmodel.DetailViewModel

class FollowFragment : Fragment() {

    companion object {
        const val ARG_POS = "position"
        const val ARG_USERNAME = "username"
        private const val TAG = "FollowFragment"
    }

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private var position: Int = -1
    private var username: String = ""
    private var followers: List<GithubUser> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        arguments?.let {
            position = it.getInt(ARG_POS)
            username = it.getString(ARG_USERNAME).toString()
        }
        detailViewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            showLoading(isLoading)
        }
        if (position == 1) {
            detailViewModel.getFollowers(username)
            detailViewModel.githubFollowers.observe(viewLifecycleOwner){ followers ->
                binding.rvFollowers.apply {
                    adapter = GithubUserAdapter{

                    }.apply {
                        submitList(followers.map {user ->
                            GithubUserEntity(
                                login = user.login,
                                avatarUrl = user.avatarUrl,
                                url = user.url,
                                isFavorite = false,
                                bio = "",
                                followers = -1,
                                following = -1,
                                name = ""
                            )
                        })
                    }
                    layoutManager = LinearLayoutManager(requireActivity())
                }
            }
        } else {
            detailViewModel.getFollowing(username)
            detailViewModel.githubFollowings.observe(viewLifecycleOwner){ followings ->
                binding.rvFollowers.apply {
                    adapter = GithubUserAdapter{

                    }.apply {
                        submitList(followings.map {user ->
                            GithubUserEntity(
                                login = user.login,
                                avatarUrl = user.avatarUrl,
                                url = user.url,
                                isFavorite = false,
                                bio = "",
                                followers = -1,
                                following = -1,
                                name = ""
                            )
                        })
                    }
                    layoutManager = LinearLayoutManager(requireActivity())
                }
            }
        }
        binding.rvFollowers.apply {
            binding.rvFollowers.apply {
                adapter = GithubUserAdapter {

                }.apply {
                    submitList(followers.map { user ->
                        GithubUserEntity(
                            login = user.login,
                            avatarUrl = user.avatarUrl,
                            url = user.url,
                            isFavorite = false,
                            bio = "",
                            followers = -1,
                            following = -1,
                            name = ""
                        )
                    })
                }
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}