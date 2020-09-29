package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Area
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class AreaAdapter(private val listener: OnItemClickListener.AreaListener) :
    RecyclerView.Adapter<AreaAdapter.ViewHolder>() {

    private var areas = emptyList<Area>()
    private var areaList: ArrayList<Area> = ArrayList()

    fun addItems(list: List<Area>) {
        areas = list
        areaList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(areaList[position], listener)
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(a: Area, listener: OnItemClickListener.AreaListener) = with(itemView) {
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
                areaList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    areaList.addAll(areas)
                } else {
                    val filteredList = ArrayList<Area>()
                    for (area: Area in areas) {
                        if (area.areaId.toLowerCase(Locale.getDefault()).contains(keyword) || area.descripcion.toLowerCase(Locale.getDefault()).contains(
                                keyword
                            )
                        ) {
                            filteredList.add(area)
                        }
                    }
                    areaList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}