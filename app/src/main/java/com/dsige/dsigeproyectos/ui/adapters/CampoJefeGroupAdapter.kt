package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.CampoJefe
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_campo_jefe_group.view.*

class CampoJefeGroupAdapter(var listener: OnItemClickListener.CampoJefeListener) :
    RecyclerView.Adapter<CampoJefeGroupAdapter.ViewHolder>() {

    private var parte = emptyList<CampoJefe>()

    fun addItems(list: List<CampoJefe>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_campo_jefe_group, parent, false)
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
            textView1.text = p.articulo
            textView2.text = p.nombreArticulo
            textView30.text = String.format("%s", p.cantidadPedida)
            textView31.text = String.format("%s", p.cantidadAprobada)
            textView31.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}