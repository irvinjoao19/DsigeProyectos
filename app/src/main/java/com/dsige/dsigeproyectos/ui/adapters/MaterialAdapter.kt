package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Material
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo_search.view.*
import java.util.*
import kotlin.collections.ArrayList

class MaterialAdapter(private var listener: OnItemClickListener.MaterialListener) :
    RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {

    private var materiales = emptyList<Material>()
    private var materialesList: ArrayList<Material> = ArrayList()

    fun addItems(list: List<Material>) {
        materiales = list
        materialesList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_combo_search, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(materialesList[position], listener)
    }

    override fun getItemCount(): Int {
        return materialesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Material, listener: OnItemClickListener.MaterialListener) = with(itemView) {
            textViewGuiaSalida.visibility = View.VISIBLE
            textViewGuiaSalida.text = m.guiaSalida
            textViewNombre.text = m.descripcion
            textViewId.text = m.materialId
            textViewAbreviatura.text = m.abreviatura
            textViewStock.text = String.format("S.T. %s", m.stock)
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                materialesList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    materialesList.addAll(materiales)
                } else {
                    val filteredList = ArrayList<Material>()
                    for (m: Material in materiales) {
                        if (m.materialId.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.descripcion.toLowerCase(Locale.getDefault()).contains(keyword)
                        ) {
                            filteredList.add(m)
                        }
                    }
                    materialesList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}