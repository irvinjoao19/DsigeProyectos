package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.Estado

import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class EstadoAdapter(var listener: OnItemClickListener.EstadoListener) :
    RecyclerView.Adapter<EstadoAdapter.ViewHolder>() {

    private var states = emptyList<Estado>()
    private var estadoList: ArrayList<Estado> = ArrayList()

    fun addItems(list: List<Estado>) {
        states = list
        estadoList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(estadoList[position], listener)
    }

    override fun getItemCount(): Int {
        return estadoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(e: Estado, listener: OnItemClickListener.EstadoListener) = with(itemView) {
            textViewNombre.text = e.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(e, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                estadoList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    estadoList.addAll(states)
                } else {
                    val filteredList = ArrayList<Estado>()
                    for (estado: Estado in states) {
                        if (estado.codigo.toLowerCase(Locale.getDefault()).contains(keyword) || estado.nombre.toLowerCase(
                                Locale.getDefault()).contains(
                                keyword
                            )
                        ) {
                            filteredList.add(estado)
                        }
                    }
                    estadoList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}