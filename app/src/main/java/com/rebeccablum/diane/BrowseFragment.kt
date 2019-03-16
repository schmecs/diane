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
import androidx.recyclerview.widget.RecyclerView
import com.rebeccablum.diane.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {

    private lateinit var binding: FragmentBrowseBinding
    private lateinit var viewModel: BrowseViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBrowseBinding.inflate(inflater, container, false).apply {
            viewModel = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupAdapter()

        viewModel = (activity as HomeActivity).getBrowseViewModel()
        viewModel.getPosts().observe(this, Observer<List<Post>> { postList: List<Post>? ->
            Log.d("Testing", postList?.size.toString())
            if (postList != null) {
                adapter.updatePostItems(postList)
                showToast()
            }
        })
    }

    private fun setupAdapter() {
        adapter = PostsAdapter()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = adapter
    }

    private fun showToast() {
        Toast.makeText(this.activity, "Observed change!", Toast.LENGTH_LONG).show()
    }
}
