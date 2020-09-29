package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.MenuPrincipal
import com.dsige.dsigeproyectos.helper.Util
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardview_firm.view.*
import java.io.File

class FirmAdapter : RecyclerView.Adapter<FirmAdapter.ViewHolder>() {

    private var menus = emptyList<MenuPrincipal>()

    fun addItems(list: List<MenuPrincipal>) {
        menus = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_firm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menus[position])
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: MenuPrincipal) = with(itemView) {
            val url = Util.UrlFoto + m.title
            Picasso.get()
                    .load(url)
                    .into(imageViewFirm, object : Callback {
                        override fun onSuccess() {
                            progress.visibility = View.GONE
                            imageViewFirm.visibility = View.VISIBLE
                        }

                        override fun onError(e: Exception) {
                            val f = File(Util.getFolder(itemView.context),m.title)
                            Picasso.get()
                                    .load(f)
                                    .into(imageViewFirm, object : Callback {
                                        override fun onSuccess() {
                                            progress.visibility = View.GONE
                                            imageViewFirm.visibility = View.VISIBLE
                                        }

                                        override fun onError(e: Exception) {
                                            imageViewFirm.visibility = View.VISIBLE
                                        }
                                    })
                        }
                    })
        }
    }
}