package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoDetalle
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_requerimiento_detalle.view.*

class RequerimientoDetalleAdapter(var listener: OnItemClickListener.RequerimientoDetalleListener) :
    RecyclerView.Adapter<RequerimientoDetalleAdapter.ViewHolder>() {

    private var parte = emptyList<RequerimientoDetalle>()

    fun addItems(list: List<RequerimientoDetalle>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_requerimiento_detalle, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: RequerimientoDetalle, listener: OnItemClickListener.RequerimientoDetalleListener) =
            with(itemView) {
                textView.text = String.format("Material : %s", p.material)
                textView2.text = p.descripionMaterial
                textView3.text = String.format("Cantidad : %s", p.cantidad)
                imgEdit.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                imgDelete.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}