package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Material
import kotlinx.android.synthetic.main.cardview_combo_search.view.*

class MaterialPaginationAdapter(
    var tipoMaterialSolicitud: Int, var solicitudId: Int,
    var listener: OnItemClickListener.MaterialListener
) :
    RecyclerView.Adapter<MaterialPaginationAdapter.ViewHolder>() {

    private var materiales = emptyList<Material>()

    fun addItems(list: List<Material>) {
        materiales = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_combo_search, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listener.let { holder.bind(materiales[position], it) }
    }

    override fun getItemCount(): Int {
        return materiales.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Material, listener: OnItemClickListener.MaterialListener) = with(itemView) {
            when (solicitudId) {
                1 -> if (tipoMaterialSolicitud == 2) {
                    textViewStock.visibility = View.GONE
                }
                2 -> {
                    textViewGuiaSalida.visibility = View.VISIBLE
                    textViewGuiaSalida.text = m.fecha
                }
            }
            textViewNombre.text = m.descripcion
            textViewId.text = m.materialId
            textViewAbreviatura.text = m.abreviatura
            textViewStock.text = String.format("S.T. %s", m.stock)
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}