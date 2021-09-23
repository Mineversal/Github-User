package com.mineversal.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mineversal.githubuser.R
import com.mineversal.githubuser.databinding.ActivityUserDetailApiBinding
import com.mineversal.githubuser.model.UserDetailResponse
import com.mineversal.githubuser.viewmodel.UserDetailViewModel

class UserDetailApiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailApiBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        username?.let { userDetailViewModel.setUserDetail(it) }

        userDetailViewModel.user.observe(this, { user ->
            setUserData(user)
        })

        userDetailViewModel.isLoading.observe(this, {
            showLoading(it)
        })


        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f


        //Action Bar
        val actionbar = supportActionBar

        //Set Action Bar Title
        actionbar?.title = "Detail User"

        //Set Back Button
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    //Back Button Up Navigation
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUserData(user: UserDetailResponse){
        binding.apply {
            name.text = user.name
            username.text = user.login
            Glide.with(this@UserDetailApiActivity)
                .load(user.avatar_url)
                .into(avatar)
            company.text = user.company
            location.text = user.location
            repository.text = getString(R.string.repo, user.public_repos.toString())
            follow.text = StringBuilder(user.followers.toString()).append(" followers • ").append(user.following.toString()).append(" following")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    //Intent Key Value Declaration
    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}