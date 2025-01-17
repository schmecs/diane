package com.rebeccablum.diane.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rebeccablum.diane.databinding.PostItemBinding
import com.rebeccablum.diane.media.PlaybackManager
import com.rebeccablum.diane.models.Post
import com.rebeccablum.diane.viewmodels.PostItemViewModel

class PostsAdapter(private val playbackManager: PlaybackManager) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    private val posts = arrayListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(PostItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    fun updatePostItems(newList: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(posts, newList))
        posts.clear()
        posts.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.postItemViewModel = PostItemViewModel(post, playbackManager)
        }
    }

    inner class PostDiffCallback(private val oldList: List<Post>, private val newList: List<Post>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].content == newList[newItemPosition].content
        }
    }
}
