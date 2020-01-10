package com.rebeccablum.diane

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rebeccablum.diane.databinding.ActivityHomeBinding
import com.rebeccablum.diane.models.Post
import com.rebeccablum.diane.viewmodels.AddPostViewModel
import com.rebeccablum.diane.viewmodels.HomeViewModel
import com.rebeccablum.diane.viewmodels.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    companion object {
        private val TAG = HomeActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityHomeBinding

    val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel

        setupViewFragment()
        setupFab()
    }

    fun getAddPostViewModel(): AddPostViewModel {
        return ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(AddPostViewModel::class.java)
            .apply { this.setCallbacks(::onPostSaved, ::onSaveError) }
    }

    private fun onPostSaved(postText: String, fileName: String) {
        Log.d(TAG, "Post content: $postText")
        viewModel.addPost(Post(content = postText, fileName = fileName))
    }

    private fun onSaveError(error: Throwable) {
        // TODO show error dialog
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
        val dialog = AddPostDialog()
        dialog.show(supportFragmentManager, AddPostDialog.TAG)
    }
}
