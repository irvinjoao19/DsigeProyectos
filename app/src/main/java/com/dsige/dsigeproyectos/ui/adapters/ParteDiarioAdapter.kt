package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.ParteDiario
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_parte_diario.view.*

class ParteDiarioAdapter(var listener: OnItemClickListener.ParteDiarioListener) : RecyclerView.Adapter<ParteDiarioAdapter.ViewHolder>() {

    private var parte = emptyList<ParteDiario>()

    fun addItems(list: List<ParteDiario>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_pedido, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: ParteDiario, listener: OnItemClickListener.ParteDiarioListener) = with(itemView) {
            textViewParteDiario.text = String.format("Nro Parte Diario : %s", p.parteDiarioId)
            textViewFecha.text = String.format("Fecha Recepción : %s", p.fecha)
            textViewObra.text = String.format("Obra : %s", p.obraTd)
            textViewEstado.text = if (p.estadoCodigo == "120") "Registrado" else "Enviado A Aprobación"
            textViewSuministro.text = String.format("Suministro : %s", p.suministro)
            textViewNroFicha.text = String.format("Nro Ficha : %s", p.sed)
            itemView.setOnClickListener { v -> listener.onItemClick(p, adapterPosition, v) }
        }
    }
}