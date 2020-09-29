package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Baremo
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo_search.view.*

class BaremoAdapter(private var listener: OnItemClickListener.BaremoListener) :
    RecyclerView.Adapter<BaremoAdapter.ViewHolder>() {

    private var baremos = emptyList<Baremo>()
    private var baremosList: ArrayList<Baremo> = ArrayList()

    fun addItems(list: List<Baremo>) {
        baremos = list
        baremosList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_combo_search, parent, false)
        return ViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(baremosList[position], listener)
    }

    override fun getItemCount(): Int {
        return baremosList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(b: Baremo, listener: OnItemClickListener.BaremoListener) = with(itemView) {
            textViewStock.visibility = View.GONE
            textViewNombre.text = b.descripcion
            textViewId.text = b.baremoId
            textViewAbreviatura.text = b.abreviatura
            itemView.setOnClickListener { v -> listener.onItemClick(b, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                baremosList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    baremosList.addAll(baremos)
                } else {
                    val filteredList = ArrayList<Baremo>()
                    for (b: Baremo in baremos) {
                        if (b.baremoId.toLowerCase().contains(keyword) || b.descripcion.toLowerCase().contains(keyword)) {
                            filteredList.add(b)
                        }
                    }
                    baremosList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}