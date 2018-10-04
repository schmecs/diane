package com.rebeccablum.diane

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rebeccablum.diane.databinding.PostItemBinding

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {
    private val posts = arrayListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostItemBinding.inflate(inflater)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    fun updatePostItems(newList: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(posts, newList))
        posts.clear()
        posts.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class PostViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
//            binding.executePendingBindings()
        }
    }

    inner class PostDiffCallback(private val oldList: List<Post>, private val newList: List<Post>) : DiffUtil.Callback() {
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
