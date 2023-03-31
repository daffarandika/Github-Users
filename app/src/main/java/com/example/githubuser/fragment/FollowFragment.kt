package com.example.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.GithubFollowAdapter
import com.example.githubuser.adapter.GithubUserAdapter
import com.example.githubuser.database.GithubUserDatabase
import com.example.githubuser.database.GithubUserRepository
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.model.local.GithubUserHeader
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.createFactory

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val apiService = ApiConfig.getApiService()
        val dao = GithubUserDatabase.getInstance(requireActivity()).getDao()
        val detailViewModelFactory = DetailViewModel(GithubUserRepository.getInstance(apiService, dao)).createFactory()
        detailViewModel = ViewModelProvider(this, detailViewModelFactory)[DetailViewModel::class.java]
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        arguments?.let {
            position = it.getInt(ARG_POS)
            username = it.getString(ARG_USERNAME).toString()
        }
        if (position == 1) {
            detailViewModel.getFollowers(username)
            detailViewModel.githubFollowers.observe(viewLifecycleOwner) { followers ->
                if (followers is com.example.githubuser.model.Result.Success) {
                    Log.e(TAG, "onCreateView: $followers")
                    binding.rvFollowers.apply {
                        this.adapter = GithubFollowAdapter(followers.data)
                    }
                }
            }
        } else {
            detailViewModel.githubFollowings.observe(viewLifecycleOwner) { followings ->
                if (followings is com.example.githubuser.model.Result.Success) {
                    Log.e(TAG, "onCreateView: $followings")
                    binding.rvFollowers.apply {
                        this.adapter = GithubFollowAdapter(followings.data)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
