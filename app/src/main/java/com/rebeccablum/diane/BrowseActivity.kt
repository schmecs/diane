package com.rebeccablum.diane

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.rebeccablum.diane.databinding.ActivityBrowseBinding

class BrowseActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(BrowseViewModel::class.java)

        val binding: ActivityBrowseBinding = DataBindingUtil.setContentView(this, R.layout.activity_browse)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        adapter = PostsAdapter()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.getPosts().observe(this, Observer<List<Post>> { postList: List<Post>? ->
            Log.d("Testing", postList?.size.toString())
            if (postList != null) { // TODO how to do this with .let?
                adapter.updatePostItems(postList) // TODO DiffUtil
                showToast()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_browse, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showToast() {
        Toast.makeText(this, "Observed change!", Toast.LENGTH_LONG).show()
    }
}
