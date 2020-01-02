package com.rebeccablum.diane

import android.content.pm.PackageManager
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

class HomeActivity : AddPostDialog.PostResultListener, AppCompatActivity() {

    companion object {
        private val TAG = HomeActivity::class.java.simpleName

        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private var permissionsAccepted = false

    private lateinit var binding: ActivityHomeBinding
    // TODO right way to do this
    lateinit var viewModel: HomeViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionsAccepted) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
            .get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        setupViewFragment()
        setupFab()
    }

    override fun onPostSaved(postText: String, fileName: String) {
        Log.d(TAG, "Post content: $postText")
        viewModel.addPost(Post(content = postText, fileName = fileName))
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
