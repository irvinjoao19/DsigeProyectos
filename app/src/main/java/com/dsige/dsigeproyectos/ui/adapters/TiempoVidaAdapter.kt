package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.TiempoVida
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_tiempo_vida.view.*

class TiempoVidaAdapter(var listener: OnItemClickListener.TiempoVidaListener) :
    RecyclerView.Adapter<TiempoVidaAdapter.ViewHolder>() {

    private var parte = emptyList<TiempoVida>()

    fun addItems(list: List<TiempoVida>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tiempo_vida, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TiempoVida, listener: OnItemClickListener.TiempoVidaListener) = with(itemView) {
            textView1.text = p.pedcNumero
            textView2.text = p.obraCodigo
            textView3.text = p.almaCodigo
            textView4.text = p.nomApellidos
            textView5.text = p.nombreOt
            textView6.text = String.format("Fec Ped.: %s", p.pedcFechaEnvio)
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}