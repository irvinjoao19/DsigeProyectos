package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudPhoto
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardview_photo.view.*
import java.io.File

class PhotoSolicitudAdapter(private var listener: OnItemClickListener.SolicitudRegistroPhotoListener) :
    RecyclerView.Adapter<PhotoSolicitudAdapter.ViewHolder>() {

    private var photos = emptyList<RegistroSolicitudPhoto>()

    fun addItems(list: List<RegistroSolicitudPhoto>) {
        photos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_photo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position], listener)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            p: RegistroSolicitudPhoto,
            listener: OnItemClickListener.SolicitudRegistroPhotoListener
        ) = with(itemView) {
            val url = Util.UrlFoto + p.nombre
            Picasso.get()
                .load(url)
                .into(imageViewPhoto, object : Callback {
                    override fun onSuccess() {
                        progress.visibility = View.GONE
                    }

                    override fun onError(e: Exception) {
                        val f = File(Util.getFolder(itemView.context), p.nombre)
                        Picasso.get()
                            .load(f)
                            .into(imageViewPhoto, object : Callback {
                                override fun onSuccess() {
                                    progress.visibility = View.GONE
                                }

                                override fun onError(e: Exception) {

                                }
                            })
                    }
                })
            textViewName.text = p.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}