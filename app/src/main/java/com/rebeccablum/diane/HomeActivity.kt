package com.rebeccablum.diane

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.rebeccablum.diane.databinding.ActivityHomeBinding

class HomeActivity : AddPostDialog.PostResultListener, AppCompatActivity() {

    companion object {
        const val TAG = "Home Activity"
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(HomeViewModel::class.java)

        setupViewFragment()
        setupFab()
    }

    override fun onPostSaved(postText: String) {
        Log.d(TAG, "Post content: $postText")
        binding.viewModel?.addPost(Post(content = postText))
    }

    override fun onPostCancelled() {
        binding.viewModel?.setDoneAddingPost()
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame)
            ?: supportFragmentManager.beginTransaction().replace(
                R.id.contentFrame,
                BrowseFragment()
            ).commit()
    }

    fun getBrowseViewModel(): BrowseViewModel {
        return ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(BrowseViewModel::class.java)
    }

    fun getAddPostViewModel(): AddPostViewModel {
        return ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(AddPostViewModel::class.java)
    }

    private fun setupFab() {
        binding.viewModel?.isAddingPost?.onChanged { addingPost ->
            if (addingPost) {
                Log.d(TAG, "Adding post")
                showNewPostDialog()
            }
        }
    }

    private fun showNewPostDialog() {
        val dialog = AddPostDialog.newInstance()
        dialog.show(supportFragmentManager, AddPostDialog.TAG)
    }
}
