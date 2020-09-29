package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Coordinador
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_personal_search.view.*
import java.util.*
import kotlin.collections.ArrayList

class CoordinadorAdapter(private var listener: OnItemClickListener.CoordinadorListener) :
    RecyclerView.Adapter<CoordinadorAdapter.ViewHolder>() {

    private var coordinadors = emptyList<Coordinador>()
    private var coordinadorList: ArrayList<Coordinador> = ArrayList()

    fun addItems(list: List<Coordinador>) {
        coordinadors = list
        coordinadorList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_personal_search, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coordinadorList[position], listener)
    }

    override fun getItemCount(): Int {
        return coordinadorList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Coordinador, listener: OnItemClickListener.CoordinadorListener) = with(itemView) {
            textViewDni.text = p.codigo
            textViewNombre.text = String.format("%s", p.nombre)
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                coordinadorList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    coordinadorList.addAll(coordinadors)
                } else {
                    val filteredList = ArrayList<Coordinador>()
                    for (c: Coordinador in coordinadors) {
                        val nombre = String.format("%s", c.nombre)
                        if (nombre.toLowerCase(Locale.getDefault()).contains(keyword) || c.codigo.toLowerCase(Locale.getDefault()).contains(keyword)) {
                            filteredList.add(c)
                        }
                    }
                    coordinadorList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}