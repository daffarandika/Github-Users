package com.example.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        detailViewModel.getFollowers(username)
        detailViewModel.getFollowing(username)
        if (position == 1) {
            detailViewModel.githubFollowers.observe(viewLifecycleOwner) { res ->
                 when (res) {
                     is com.example.githubuser.model.Result.Success -> {
                         binding.pbFollow.visibility = View.GONE
                         binding.rvFollowers.apply {
                             this.adapter = GithubFollowAdapter(res.data)
                             this.layoutManager = LinearLayoutManager(requireActivity())
                         }
                     }
                     is com.example.githubuser.model.Result.Error -> {
                         binding.pbFollow.visibility = View.GONE
                         Toast.makeText(requireActivity(), res.message, Toast.LENGTH_SHORT).show()
                     }
                     else -> {
                         binding.pbFollow.visibility = View.VISIBLE
                         Toast.makeText(requireActivity(), "cok", Toast.LENGTH_SHORT).show()
                     }
                 }
            }
        } else {
            detailViewModel.githubFollowings.observe(viewLifecycleOwner) { res ->
                when (res) {
                    is com.example.githubuser.model.Result.Success -> {
                        binding.pbFollow.visibility = View.GONE
                        binding.rvFollowers.apply {
                            this.adapter = GithubFollowAdapter(res.data)
                            this.layoutManager = LinearLayoutManager(requireActivity())
                        }
                    }
                    is com.example.githubuser.model.Result.Error -> {
                        binding.pbFollow.visibility = View.GONE
                        Toast.makeText(requireActivity(), res.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.pbFollow.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), "cok", Toast.LENGTH_SHORT).show()
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

    companion object {
        const val ARG_POS = "position"
        const val ARG_USERNAME = "username"
        private const val TAG = "FollowFragment"
    }


}
