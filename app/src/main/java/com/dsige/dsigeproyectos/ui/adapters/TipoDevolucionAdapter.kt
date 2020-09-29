package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.TipoDevolucion
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class TipoDevolucionAdapter(private var listener: OnItemClickListener.TipoDevolucionListener) :
    RecyclerView.Adapter<TipoDevolucionAdapter.ViewHolder>() {

    private var devoluciones = emptyList<TipoDevolucion>()
    private var devolucionesList: ArrayList<TipoDevolucion> = ArrayList()

    fun addItems(list: List<TipoDevolucion>) {
        devoluciones = list
        devolucionesList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(devolucionesList[position], listener)
    }

    override fun getItemCount(): Int {
        return devolucionesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(s: TipoDevolucion, listener: OnItemClickListener.TipoDevolucionListener) = with(itemView) {
            textViewNombre.text = s.descripcion
            itemView.setOnClickListener { v -> listener.onItemClick(s, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                devolucionesList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    devolucionesList.addAll(devoluciones)
                } else {
                    val filteredList = ArrayList<TipoDevolucion>()
                    for (s: TipoDevolucion in devoluciones) {
                        if (s.descripcion.toLowerCase(Locale.getDefault()).contains(keyword)) {
                            filteredList.add(s)
                        }
                    }
                    devolucionesList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}