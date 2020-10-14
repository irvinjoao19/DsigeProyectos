package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Requerimiento
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_requerimiento.view.*

class RequerimientoAdapter(var listener: OnItemClickListener.RequerimientoListener) :
    RecyclerView.Adapter<RequerimientoAdapter.ViewHolder>() {

    private var parte = emptyList<Requerimiento>()

    fun addItems(list: List<Requerimiento>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_requerimiento, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Requerimiento, listener: OnItemClickListener.RequerimientoListener) =
            with(itemView) {
                textView.text = String.format("Nro Solicitud : %s", p.nroSolicitud)
                textView2.text = p.fecha
                textView3.text = String.format("C.Costo : %s",p.nombreCentroCosto)
                textView4.text = p.observaciones
                textView5.text = p.estado
                imgDelete.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}