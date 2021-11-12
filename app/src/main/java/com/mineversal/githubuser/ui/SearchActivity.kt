package com.mineversal.githubuser.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mineversal.githubuser.adapter.UserAdapter
import com.mineversal.githubuser.databinding.ActivitySearchBinding
import com.mineversal.githubuser.model.Users
import com.mineversal.githubuser.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        searchViewModel.setSearchUsers(username!!)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        searchViewModel.listUsers.observe(this, { search ->
            getSearch(search)
        })

        searchViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }

        //Action Bar
        val actionbar = supportActionBar

        //Set Action Bar Title
        actionbar?.title = "Hasil Pencarian"

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

    private fun getSearch(searchUser: ArrayList<Users>) {
        val listUser = ArrayList<Users>()
        if (searchUser.size >= 1) {
            listUser.clear()
            listUser.addAll(searchUser)
            val adapterUserAdapter = UserAdapter(listUser)
            binding.rvUser.adapter = adapterUserAdapter

            adapterUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {
                    showUserDetails(data)
                }
            })
        } else {
            binding.NotFind.visibility = View.VISIBLE
        }
    }

    private fun showUserDetails(user: Users) {
        val moveWithObjectIntent = Intent(this@SearchActivity, UserDetailApiActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailApiActivity.EXTRA_USERNAME, user.login)
        startActivity(moveWithObjectIntent)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}