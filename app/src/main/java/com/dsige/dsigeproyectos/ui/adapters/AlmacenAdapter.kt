package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Almacen
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class AlmacenAdapter(private val listener: OnItemClickListener.AlmacenListener) :
        RecyclerView.Adapter<AlmacenAdapter.ViewHolder>() {

    private var almacen = emptyList<Almacen>()
    private var almacenList: ArrayList<Almacen> = ArrayList()

    fun addItems(list: List<Almacen>) {
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
        fun bind(a: Almacen, listener: OnItemClickListener.AlmacenListener) = with(itemView) {
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
                almacenList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    almacenList.addAll(almacen)
                } else {
                    val filteredList = ArrayList<Almacen>()
                    for (a: Almacen in almacen) {
                        if (a.descripcion.toLowerCase(Locale.getDefault()).contains(keyword) || a.opCodigo.toLowerCase(Locale.getDefault()).contains(keyword)) {
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