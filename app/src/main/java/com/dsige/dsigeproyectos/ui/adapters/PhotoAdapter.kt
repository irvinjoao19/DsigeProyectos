package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardview_photo.view.*
import java.io.File

class PhotoAdapter(private var listener: OnItemClickListener.MenuListener) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private var photos = emptyList<MenuPrincipal>()

    fun addItems(list: List<MenuPrincipal>) {
        photos = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position], listener)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_photo, parent, false)
        return ViewHolder(v!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(p: MenuPrincipal, listener: OnItemClickListener.MenuListener) =
            with(itemView) {
                val url = File(Util.getFolder(itemView.context), p.title)
                Picasso.get()
                    .load(url)
                    .into(imageViewPhoto, object : Callback {
                        override fun onSuccess() {
                            progress.visibility = View.GONE
                            imageViewPhoto.visibility = View.VISIBLE
                        }

                        override fun onError(e: Exception) {

                        }
                    })
                textViewName.visibility = View.GONE
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}