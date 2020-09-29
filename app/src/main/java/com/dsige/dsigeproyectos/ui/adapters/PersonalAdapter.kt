package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Personal
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_personal_search.view.*
import java.util.*
import kotlin.collections.ArrayList

class PersonalAdapter(private var listener: OnItemClickListener.PersonalListener) :
    RecyclerView.Adapter<PersonalAdapter.ViewHolder>() {

    private var personals = emptyList<Personal>()
    private var personalList: ArrayList<Personal> = ArrayList()

    fun addItems(list: List<Personal>) {
        personals = list
        personalList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_personal_search, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(personalList[position], listener)
    }

    override fun getItemCount(): Int {
        return personalList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Personal, listener: OnItemClickListener.PersonalListener) = with(itemView) {
            textViewDni.text = p.nroDocumento
            textViewNombre.text = String.format("%s %s", p.nombre, p.apellido)
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                personalList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    personalList.addAll(personals)
                } else {
                    val filteredList = ArrayList<Personal>()
                    for (p: Personal in personals) {
                        val nombre = String.format("%s %s", p.nombre, p.apellido)
                        if (nombre.toLowerCase(Locale.getDefault()).contains(keyword) || p.nroDocumento.toLowerCase(Locale.getDefault()).contains(keyword)) {
                            filteredList.add(p)
                        }
                    }
                    personalList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}