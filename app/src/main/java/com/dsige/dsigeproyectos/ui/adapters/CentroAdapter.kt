package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.CentroCostos
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class CentroAdapter(private var listener: OnItemClickListener.CentroCostosListener) :
    RecyclerView.Adapter<CentroAdapter.ViewHolder>() {

    private var centros = emptyList<CentroCostos>()
    private var centrosList: ArrayList<CentroCostos> = ArrayList()

    fun addItems(list: List<CentroCostos>) {
        centros = list
        centrosList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(centrosList[position], listener)
    }

    override fun getItemCount(): Int {
        return centrosList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(c: CentroCostos, listener: OnItemClickListener.CentroCostosListener) =
            with(itemView) {
                textViewNombre.text = c.descripcion
                itemView.setOnClickListener { v -> listener.onItemClick(c, v, adapterPosition) }
            }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                centrosList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    centrosList.addAll(centros)
                } else {
                    val filteredList = ArrayList<CentroCostos>()
                    for (centro: CentroCostos in centros) {
                        if (centro.centroId.toLowerCase(Locale.getDefault()).contains(keyword) || centro.descripcion.toLowerCase(
                                Locale.getDefault()
                            ).contains(
                                keyword
                            )
                        ) {
                            filteredList.add(centro)
                        }
                    }
                    centrosList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}