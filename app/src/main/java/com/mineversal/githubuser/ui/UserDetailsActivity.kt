package com.mineversal.githubuser.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mineversal.githubuser.R
import com.mineversal.githubuser.model.User

class UserDetailsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        //Get Data
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val bundle = Bundle()
        bundle.putString(UserDetailApiActivity.EXTRA_USERNAME, user.username)

        //Activity Content Id Declaration
        val btnGithub: Button = findViewById(R.id.github)
        btnGithub.setOnClickListener(this)
        val btnShare: Button = findViewById(R.id.share)
        btnShare.setOnClickListener(this)
        val imageView: ImageView = findViewById(R.id.avatar)
        val nameReceived: TextView = findViewById(R.id.name)
        val usernameReceived: TextView = findViewById(R.id.username)
        val followReceived: TextView = findViewById(R.id.follow)
        val companyReceived: TextView = findViewById(R.id.company)
        val locationReceived: TextView = findViewById(R.id.location)
        val repositoryReceived: TextView = findViewById(R.id.repository)

        val resId: Int = user.avatar
        val name = user.name
        val username = user.username
        val follower = user.follower.toString()
        val following = user.following.toString()
        val company = user.company
        val location = user.location
        val repository = user.repository.toString()

        //Set Data to Activity View Content
        imageView.setImageResource(resId)
        nameReceived.text = name
        usernameReceived.text = username
        followReceived.text = "$follower followers • $following following"
        companyReceived.text = company
        locationReceived.text = location
        repositoryReceived.text = getString(R.string.repo, repository)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
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