package com.mineversal.githubuser.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mineversal.githubuser.R
import com.mineversal.githubuser.data.database.FavoriteUser
import com.mineversal.githubuser.databinding.ActivityUserDetailsBinding
import com.mineversal.githubuser.data.model.User
import com.mineversal.githubuser.data.viewmodel.UserDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailsBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()
    private var favoriteUser: FavoriteUser? = FavoriteUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Data
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val bundle = Bundle()
        bundle.putString(UserDetailApiActivity.EXTRA_USERNAME, user.username)

        //Activity Content Id Declaration
        val btnGithub: Button = binding.github
        btnGithub.setOnClickListener(this)
        val btnShare: Button = binding.share
        btnShare.setOnClickListener(this)
        val imageView: ImageView = binding.avatar
        val nameReceived: TextView = binding.name
        val usernameReceived: TextView = binding.username
        val followReceived: TextView = binding.follow
        val companyReceived: TextView = binding.company
        val locationReceived: TextView = binding.location
        val repositoryReceived: TextView = binding.repository

        val resId: Int = user.avatar
        val name = user.name
        val username = user.username
        val follower = user.follower.toString()
        val following = user.following.toString()
        val company = user.company
        val location = user.location
        val repository = user.repository.toString()

        //Set Data to Activity View Content
        Glide.with(this)
            .load(resId)
            .into(imageView)
        nameReceived.text = name
        usernameReceived.text = username
        followReceived.text = StringBuilder(follower).append(" followers • ").append(following).append(" following")
        companyReceived.text = company
        locationReceived.text = location
        repositoryReceived.text = getString(R.string.repo, repository)

        favoriteUser?.login = username
        favoriteUser?.id = user.id
        favoriteUser?.avatar_url = user.avatar_url

        var isChecked = false

        CoroutineScope(Dispatchers.IO).launch {
            val count = userDetailViewModel.checkUser(user.id)
            withContext(Dispatchers.Main){
                if (count > 0) {
                    binding.toggleFavorite.isChecked = true
                    isChecked = true
                } else {
                    binding.toggleFavorite.isChecked = false
                    isChecked = false
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked){
                userDetailViewModel.insert(favoriteUser as FavoriteUser)
                showToast("User Berhasil Ditambahkan ke Favorite")
            } else {
                userDetailViewModel.delete(favoriteUser as FavoriteUser)
                showToast("User Berhasil Dihapus Dari Favorite")
            }
            binding.toggleFavorite.isChecked = isChecked
        }

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

    //Button On Click to Github Page & Share Github
    override fun onClick(v: View?) {
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        when (v?.id) {
            R.id.github -> {
                val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/${user.username}"))
                startActivity(websiteIntent)
            }
            R.id.share -> {
                val github = "https://github.com/${user.username}"
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TITLE, user.name)
                shareIntent.putExtra(Intent.EXTRA_TEXT, github)
                shareIntent.type = "text/*"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(shareIntent, user.name))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //Intent Key Value Declaration
    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}