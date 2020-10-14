package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.OrdenDetalle
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_orden_detalle.view.*

class OrdenDetalleAdapter(var listener: OnItemClickListener.OrdenDetalleListener) :
    RecyclerView.Adapter<OrdenDetalleAdapter.ViewHolder>() {

    private var parte = emptyList<OrdenDetalle>()

    fun addItems(list: List<OrdenDetalle>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_orden_detalle, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: OrdenDetalle, listener: OnItemClickListener.OrdenDetalleListener) =
            with(itemView) {
                textView1.text = String.format("Precio : %s", p.precio)
                textView2.text = p.proveedor
                textView3.text = String.format("Fecha de compra%s", p.fecha)
                textView4.text = p.razonSocial
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}