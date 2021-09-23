package com.mineversal.githubuser.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import org.json.JSONTokener
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.mineversal.githubuser.R
import com.mineversal.githubuser.adapter.ListUserAdapter
import com.mineversal.githubuser.databinding.ActivityMainBinding
import com.mineversal.githubuser.model.User
import com.mineversal.githubuser.viewmodel.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private var itemsArray: ArrayList<User> = arrayListOf()
    private lateinit var adapter: ListUserAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = binding.rvUser
        rvUser.setHasFixedSize(true)

        //Function Call
        getData()
        showRecyclerList()

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvUser.layoutManager = LinearLayoutManager(this)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUser(query)
                return true
            }
            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

        return true
    }

    //Get Data From JSON File
    private fun getData(): ArrayList<User> {
        val jsonFileString = Utils().getJsonDataFromAsset(applicationContext, "githubuser.json")
        Log.i("users", jsonFileString!!)

        val jsonObject = JSONTokener(jsonFileString).nextValue() as JSONObject

        val jsonArray = jsonObject.getJSONArray("users")

        val list = arrayListOf<User>()

        //Initialize Data to Array
        for (i in 0 until jsonArray.length()) {
            val username = jsonArray.getJSONObject(i).getString("username")
            val name = jsonArray.getJSONObject(i).getString("name")
            val avatar = jsonArray.getJSONObject(i).getString("avatar")
            val image = resources.getIdentifier(avatar, "drawable", this.packageName)
            val company = jsonArray.getJSONObject(i).getString("company")
            val location = jsonArray.getJSONObject(i).getString("location")
            val repository = jsonArray.getJSONObject(i).getInt("repository")
            val follower = jsonArray.getJSONObject(i).getInt("follower")
            val following = jsonArray.getJSONObject(i).getInt("following")

            //Call Data Model
            val model = User(
                username,
                name,
                image,
                company,
                location,
                repository,
                follower,
                following
            )
            itemsArray.add(model)

            adapter = ListUserAdapter(itemsArray)
        }

        return list
    }

    //Move to Search Activity
    private fun searchUser(username: String) {
        val moveWithObjectIntent = Intent(this@MainActivity, SearchActivity::class.java)
        moveWithObjectIntent.putExtra(SearchActivity.EXTRA_USERNAME, username)
        startActivity(moveWithObjectIntent)
    }

    //Move to Details Activity
    private fun showItemDetails(user: User) {
        val moveWithObjectIntent = Intent(this@MainActivity, UserDetailsActivity::class.java)
        moveWithObjectIntent.putExtra(UserDetailsActivity.EXTRA_USER, user)
        startActivity(moveWithObjectIntent)
    }

    //Recycler List Declaration and Call Back Function
    private fun showRecyclerList() {
        rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(itemsArray)
        rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    //Selected User
    fun showSelectedUser(user: User) {
        showItemDetails(user)
    }
}