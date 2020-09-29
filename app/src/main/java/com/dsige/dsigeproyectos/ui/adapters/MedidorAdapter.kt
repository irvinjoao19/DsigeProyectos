package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Medidor
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class MedidorAdapter(private var listener: OnItemClickListener.MedidorListener) :
    RecyclerView.Adapter<MedidorAdapter.ViewHolder>() {

    private var medidores = emptyList<Medidor>()
    private var medidoresList: ArrayList<Medidor> = ArrayList()

    fun addItems(list: List<Medidor>) {
        medidores = list
        medidoresList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(medidoresList[position], listener)
    }

    override fun getItemCount(): Int {
        return medidoresList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Medidor, listener: OnItemClickListener.MedidorListener) = with(itemView) {
            textViewNombre.text = m.medidorId
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                medidoresList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    medidoresList.addAll(medidores)
                } else {
                    val filteredList = ArrayList<Medidor>()
                    for (m: Medidor in medidores) {
                        if (m.medidorId.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.almacenCodigo.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.empleadoDni.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.guiaNumero.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.articuloCodigo.toLowerCase(Locale.getDefault()).contains(keyword)
                        ) {
                            filteredList.add(m)
                        }
                    }
                    medidoresList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}