package com.mineversal.githubuser.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mineversal.githubuser.adapter.FavoriteAdapter
import com.mineversal.githubuser.data.database.FavoriteUser
import com.mineversal.githubuser.data.helper.FavoriteUserViewModelFactory
import com.mineversal.githubuser.data.viewmodel.SearchViewModel
import com.mineversal.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllNotes().observe(this, { userList ->
            if (userList != null) {
                getSearch(userList)
            }
        })

        searchViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }

        val actionbar = supportActionBar

        //Set Action Bar Title
        actionbar?.title = "Favorite"

        //Set Back Button
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    //Back Button Up Navigation
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getSearch(favUser: List<FavoriteUser>) {
        val listUser = ArrayList<FavoriteUser>()
        if (favUser.isNotEmpty()) {
            listUser.clear()
            listUser.addAll(favUser)
            val adapterUserAdapter = FavoriteAdapter(listUser)
            binding.rvUser.adapter = adapterUserAdapter

            adapterUserAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(data: FavoriteUser) {
                    showUserDetails(data)
                }
            })
        } else {
            binding.NotFind.visibility = View.VISIBLE
        }
    }

    private fun showUserDetails(user: FavoriteUser) {
        val moveWithObjectIntent = Intent(this@FavoriteActivity, UserDetailApiActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailApiActivity.EXTRA_USERNAME, user.login)
        moveWithObjectIntent.putExtra(UserDetailApiActivity.EXTRA_ID, user.id)
        moveWithObjectIntent.putExtra(UserDetailApiActivity.EXTRA_AVATAR, user.avatar_url)
        startActivity(moveWithObjectIntent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): SearchViewModel {
        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(SearchViewModel::class.java)
    }
}