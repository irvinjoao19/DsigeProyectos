package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Obra
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo_search.view.*
import java.util.*
import kotlin.collections.ArrayList

class ObraAdapter(private var listener: OnItemClickListener.ObraListener) :
    RecyclerView.Adapter<ObraAdapter.ViewHolder>() {

    private var obras = emptyList<Obra>()
    private var obrasList: ArrayList<Obra> = ArrayList()

    fun addItems(list: List<Obra>) {
        obras = list
        obrasList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_combo_search, parent, false)
        return ViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(obrasList[position], listener)
    }

    override fun getItemCount(): Int {
        return obrasList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(o: Obra, listener: OnItemClickListener.ObraListener) = with(itemView) {
            textViewNombre.text = o.descripcion
            linearLayoutDetalle.visibility = View.GONE
            itemView.setOnClickListener { v -> listener.onItemClick(o, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                obrasList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    obrasList.addAll(obras)
                } else {
                    val filteredList = ArrayList<Obra>()
                    for (obra: Obra in obras) {
                        if (obra.obraId.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            obra.descripcion.toLowerCase(Locale.getDefault()).contains(keyword)
                        ) {
                            filteredList.add(obra)
                        }
                    }
                    obrasList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}