package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.RequerimientoTipo
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class RequerimientoTipoAdapter(private val listener: OnItemClickListener.RequerimientoTipoListener) :
        RecyclerView.Adapter<RequerimientoTipoAdapter.ViewHolder>() {

    private var almacen = emptyList<RequerimientoTipo>()
    private var almacenList: ArrayList<RequerimientoTipo> = ArrayList()

    fun addItems(list: List<RequerimientoTipo>) {
        almacen = list
        almacenList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(almacenList[position], listener)
    }

    override fun getItemCount(): Int {
        return almacenList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(a: RequerimientoTipo, listener: OnItemClickListener.RequerimientoTipoListener) = with(itemView) {
            textViewNombre.text = a.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(a, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                almacenList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    almacenList.addAll(almacen)
                } else {
                    val filteredList = ArrayList<RequerimientoTipo>()
                    for (a: RequerimientoTipo in almacen) {
                        if (a.codigo.toLowerCase(Locale.getDefault()).contains(keyword) || a.nombre.toLowerCase(Locale.getDefault()).contains(keyword)) {
                            filteredList.add(a)
                        }
                    }
                    almacenList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}