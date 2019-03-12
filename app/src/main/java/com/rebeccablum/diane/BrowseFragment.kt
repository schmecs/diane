package com.rebeccablum.diane

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rebeccablum.diane.databinding.FragmentBrowseBinding

class BrowseFragment : Fragment() {

    private lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBrowseBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as BrowseActivity).viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        setupFab()
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = PostsAdapter()
    }

    private fun setupFab() {
        // TODO: why is this nullable here & not in Google's example?
        activity?.findViewById<FloatingActionButton>(R.id.fab_add_post)?.run {
            setOnClickListener {
                Toast.makeText(activity, "Add new post clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
