package com.rebeccablum.diane

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rebeccablum.diane.databinding.ActivityHomeBinding

class HomeActivity : AddPostDialog.PostResultListener, AppCompatActivity() {

    companion object {
        const val TAG = "Home Activity"
    }

    private lateinit var binding: ActivityHomeBinding
    // TODO right way to do this
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        setupViewFragment()
        setupFab()
    }

    override fun onPostSaved(postText: String) {
        Log.d(TAG, "Post content: $postText")
        binding.viewModel?.addPost(Post(content = postText))
    }

    override fun onPostCancelled() {
        binding.viewModel?.onPostCancelled()
    }

    fun getAddPostViewModel(): AddPostViewModel {
        return ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(AddPostViewModel::class.java)
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame)
            ?: supportFragmentManager.beginTransaction().replace(
                R.id.contentFrame,
                BrowseFragment()
            ).commit()
    }

    private fun setupFab() {
        binding.viewModel?.addPostClickEvent?.observe(this, Observer {
            showNewPostDialog()
        })
    }

    private fun showNewPostDialog() {
        val dialog = AddPostDialog.newInstance()
        dialog.show(supportFragmentManager, AddPostDialog.TAG)
    }
}
