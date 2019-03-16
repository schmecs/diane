package com.rebeccablum.diane

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(HomeViewModel::class.java)

        setContentView(R.layout.activity_home)

        setupViewFragment()
        setupFab()
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame)
                ?: supportFragmentManager.beginTransaction().replace(R.id.contentFrame, BrowseFragment()).commit()
    }

    fun getBrowseViewModel(): BrowseViewModel {
        return ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(BrowseViewModel::class.java)
    }

    private fun setupFab() {
        // TODO: why is this nullable here & not in Google's example?
        findViewById<FloatingActionButton>(R.id.fab_add_post)?.run {
            setOnClickListener {
                Toast.makeText(this@HomeActivity, "Add new post clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
