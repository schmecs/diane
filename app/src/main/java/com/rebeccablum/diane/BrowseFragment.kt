package com.rebeccablum.diane

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rebeccablum.diane.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private lateinit var binding: FragmentBrowseBinding
    private lateinit var adapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBrowseBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as HomeActivity).viewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.viewModel?.let {
            adapter = PostsAdapter()
            it.postData.observe(this, Observer<List<Post>> { postList: List<Post> ->
                Log.d("Testing", postList.size.toString())
                adapter.updatePostItems(postList)
                showToast()
            })
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this.activity)
        } ?: Log.d(TAG, "Viewmodel not initialized.")
    }

    private fun showToast() {
        Toast.makeText(this.activity, "Observed change!", Toast.LENGTH_LONG).show()
    }
}
