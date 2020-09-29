package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Actividad
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class ActividadAdapter(private var listener: OnItemClickListener.ActividadListener) :
    RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    private var actividades = emptyList<Actividad>()
    private var actividadList: ArrayList<Actividad> = ArrayList()

    fun addItems(list: List<Actividad>) {
        actividades = list
        actividadList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(actividadList[position], listener)
    }

    override fun getItemCount(): Int {
        return actividadList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(a: Actividad, listener: OnItemClickListener.ActividadListener) = with(itemView) {
            textViewNombre.text = a.descripcion
            itemView.setOnClickListener { v -> listener.onItemClick(a, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                actividadList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    actividadList.addAll(actividades)
                } else {
                    val filteredList = ArrayList<Actividad>()
                    for (a: Actividad in actividades) {
                        if (a.descripcion.toLowerCase(Locale.getDefault()).contains(keyword)) {
                            filteredList.add(a)
                        }
                    }
                    actividadList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}