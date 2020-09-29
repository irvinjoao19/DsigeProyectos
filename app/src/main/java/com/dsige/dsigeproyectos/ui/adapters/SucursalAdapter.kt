package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Sucursal
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class SucursalAdapter(private var listener: OnItemClickListener.SucursalListener) :
    RecyclerView.Adapter<SucursalAdapter.ViewHolder>() {

    private var sucursales = emptyList<Sucursal>()
    private var sucursalesList: ArrayList<Sucursal> = ArrayList()

    fun addItems(list: List<Sucursal>) {
        sucursales = list
        sucursalesList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sucursalesList[position], listener)
    }

    override fun getItemCount(): Int {
        return sucursalesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(s: Sucursal, listener: OnItemClickListener.SucursalListener) = with(itemView) {
            textViewNombre.text = s.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(s, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                sucursalesList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    sucursalesList.addAll(sucursales)
                } else {
                    val filteredList = ArrayList<Sucursal>()
                    for (s: Sucursal in sucursales) {
                        if (s.nombre.toLowerCase(Locale.getDefault()).contains(keyword) || s.codigo.toLowerCase(Locale.getDefault()).contains(
                                keyword
                            )
                        ) {
                            filteredList.add(s)
                        }
                    }
                    sucursalesList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}