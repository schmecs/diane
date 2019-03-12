package com.rebeccablum.diane

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BrowseActivity : AppCompatActivity() {

    lateinit var viewModel: BrowseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        setupViewFragment()

        viewModel = ViewModelProviders.of(this).get(BrowseViewModel::class.java)
        viewModel.getPosts().observe(this, Observer<List<Post>> { postList: List<Post>? ->
            Log.d("Testing", postList?.size.toString())
            if (postList != null) {
                showToast()
            }
        })
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame)
                ?: supportFragmentManager.beginTransaction().replace(R.id.contentFrame, BrowseFragment()).commit()
    }

    private fun showToast() {
        Toast.makeText(this, "Observed change!", Toast.LENGTH_LONG).show()
    }
}
