package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Orden
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_orden_group.view.*

class OrdenGroupAdapter(var listener: OnItemClickListener.OrdenListener) :
    RecyclerView.Adapter<OrdenGroupAdapter.ViewHolder>() {

    private var parte = emptyList<Orden>()

    fun addItems(list: List<Orden>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_orden_group, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Orden, listener: OnItemClickListener.OrdenListener) = with(itemView) {
            textView1.text = p.articulo
            textView2.text = p.nombreArticulo
            textView30.text = String.format("%s", p.cantidadAprobada)
            textView31.text = String.format("%s", p.precio)
            textView22.text = String.format("Total = %.4f", p.totalOc)
            imgHistorico.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}