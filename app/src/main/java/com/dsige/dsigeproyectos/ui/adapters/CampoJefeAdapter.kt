package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.CampoJefe
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_campo_jefe.view.*

class CampoJefeAdapter(var listener: OnItemClickListener.CampoJefeListener) :
    RecyclerView.Adapter<CampoJefeAdapter.ViewHolder>() {

    private var parte = emptyList<CampoJefe>()

    fun addItems(list: List<CampoJefe>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_campo_jefe, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: CampoJefe, listener: OnItemClickListener.CampoJefeListener) = with(itemView) {
            textView1.text = p.pedcNumero
            textView2.text = p.obraCodigo
            textView3.text = p.almaDescripcion
            textView4.text = p.nomApellidos
            textView5.text = p.nombreOt
            textView6.text = String.format("Fec Ped.: %s", p.pedcFechaEnvio)
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}