package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Anulacion
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_orden.view.*

class AnulacionAdapter(var listener: OnItemClickListener.AnulacionListener) :
    RecyclerView.Adapter<AnulacionAdapter.ViewHolder>() {

    private var parte = emptyList<Anulacion>()

    fun addItems(list: List<Anulacion>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_anulacion, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Anulacion, listener: OnItemClickListener.AnulacionListener) = with(itemView) {
            textView1.text = p.toc
            textView2.text = p.nroOrden
            textView3.text = p.contableOt
            textView4.text = p.provee
            textView5.text = p.forma

            if (p.mone.contains("$")){
                textView6.setTextColor(ContextCompat.getColor(itemView.context,R.color.colorGreen))
                textView7.setTextColor(ContextCompat.getColor(itemView.context,R.color.colorGreen))
            }

            textView6.text = String.format("Total Inc IGV : %s", p.igv)
            textView7.text = p.mone
            textView8.text = p.fechaEmisionOrden
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}