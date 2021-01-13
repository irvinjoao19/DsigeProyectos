package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.MenuLogistica
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_menu.view.*

class MenuLogisticaAdapter(var listener: OnItemClickListener.MenuLogisticaListener) :
    RecyclerView.Adapter<MenuLogisticaAdapter.ViewHolder>() {

    private var menus = emptyList<MenuLogistica>()

    fun addItems(list: List<MenuLogistica>) {
        menus = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menus[position], listener)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: MenuLogistica, listener: OnItemClickListener.MenuLogisticaListener) =
            with(itemView) {
                textViewTitulo.text = m.nombre
                imageViewPhoto.setImageResource(
                    when (m.orden) {
                        1 -> R.drawable.ic_1
                        2 -> R.drawable.ic_2
                        3 -> R.drawable.ic_3
                        4 -> R.drawable.ic_4
                        else -> R.drawable.ic_5
                    }
                )
                itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
            }
    }
}