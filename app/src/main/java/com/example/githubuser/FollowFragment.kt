package com.example.githubuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.GithubUserAdapterLinear
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.model.GithubUser
import com.example.githubuser.networking.ApiConfig
import com.example.githubuser.viewmodel.DetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_POS)
            username = it.getString(ARG_USERNAME).toString()
        }
        if (position == 1) {
            detailViewModel.githubFollowers.observe(viewLifecycleOwner){ followers ->
                binding.rvFollowers.apply {
                    adapter = GithubUserAdapterLinear(followers ,requireActivity())
                    layoutManager = LinearLayoutManager(requireActivity())
                }
            }
        } else {
            detailViewModel.githubFollowings.observe(viewLifecycleOwner){ followings ->
                binding.rvFollowers.apply {
                    adapter = GithubUserAdapterLinear(followings ,requireActivity())
                    layoutManager = LinearLayoutManager(requireActivity())
                }
            }
        }
        detailViewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            showLoading(isLoading)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        arguments?.let {
            position = it.getInt(ARG_POS)
            username = it.getString(ARG_USERNAME).toString()
        }
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        binding.rvFollowers.apply {
            adapter = GithubUserAdapterLinear(followers, requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }

        val clientFollowers = ApiConfig.getApiService().getFollowers(username)
        clientFollowers.enqueue(object: Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>,
            ) {
                detailViewModel._isLoading.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        detailViewModel._githubFollowers.value = responseBody
                        detailViewModel._isLoading.value = false
                    } else Log.e(TAG, "onResponse: ${response.message()}")
                } else Log.e(TAG, "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                detailViewModel._isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

        val clientFollowings = ApiConfig.getApiService().getFollowings(username)
        clientFollowings.enqueue(object: Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>,
            ) {
                detailViewModel._isLoading.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        detailViewModel._githubFollowings.value = responseBody
                        detailViewModel._isLoading.value = false
                    } else Log.e(TAG, "onResponse: ${response.message()}")
                } else Log.e(TAG, "onResponse: ${response.message()}")
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                detailViewModel._isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading(isLoading: Boolean) {
        binding.pbFollow.visibility = if (isLoading) View.VISIBLE
                                      else View.GONE
    }

}