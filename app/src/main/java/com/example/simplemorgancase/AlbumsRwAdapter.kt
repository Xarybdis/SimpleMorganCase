package com.example.simplemorgancase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemorgancase.databinding.ListItemAlbumsBinding
import com.example.simplemorgancase.network.model.AlbumResponse

class AlbumsRwAdapter(private val context: Context) : RecyclerView.Adapter<AlbumsRwAdapter.AlbumViewHolder>() {
    private var list = mutableListOf<AlbumResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ListItemAlbumsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    /**
     * List always sorted by title.
     */
    fun updateList(data: List<AlbumResponse>) {
        list.clear()
        list.addAll(data.sortedBy { it.title })
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(private val itemBinding: ListItemAlbumsBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(albumItem: AlbumResponse) {
            itemBinding.txtAlbumId.text = albumItem.id.toString()
            itemBinding.txtUserId.text = albumItem.userId.toString()
            itemBinding.txtAlbumTitle.text = albumItem.title
        }
    }
}