package com.mineversal.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mineversal.githubuser.R
import com.mineversal.githubuser.adapter.UserAdapter
import com.mineversal.githubuser.databinding.FragmentFollowerBinding
import com.mineversal.githubuser.model.Users
import com.mineversal.githubuser.viewmodel.FollowerViewModel


class FollowerFragment: Fragment(R.layout.fragment_follower) {
    private lateinit var _binding: FragmentFollowerBinding
    private val binding get() = _binding
    private val followerViewModel by viewModels<FollowerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val username = args?.getString(UserDetailApiActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowerBinding.bind(view)

        followerViewModel.setUsers(username)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        followerViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        followerViewModel.user.observe(viewLifecycleOwner, { follower ->
            getUser(follower)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getUser(users: ArrayList<Users>) {
        val listUser = ArrayList<Users>()
        if (users.size >= 1) {
            print(users)
            listUser.clear()
            listUser.addAll(users)
            val adapterList = UserAdapter(listUser)
            binding.rvUsers.adapter = adapterList

            //Its used because it avoids crash
            adapterList.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {

                }
            })
        } else {
            binding.NotFind.visibility = View.VISIBLE
        }
    }
}