package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroMaterial
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_materiales_partediario.view.*

class ParteDiarioMaterialRegistroAdapter(private var listener: OnItemClickListener.RegistroMaterialListener) :
    RecyclerView.Adapter<ParteDiarioMaterialRegistroAdapter.ViewHolder>() {
    private var materialRegistro = emptyList<RegistroMaterial>()

    fun addItems(list: List<RegistroMaterial>) {
        materialRegistro = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_materiales_partediario, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(materialRegistro[position], listener)
    }

    override fun getItemCount(): Int {
        return materialRegistro.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: RegistroMaterial, listener: OnItemClickListener.RegistroMaterialListener) =
            with(itemView) {
                if (m.estado == "121") {
                    buttonEdit.visibility = View.GONE
                    buttonDelete.visibility = View.GONE
                    textViewCantidadAprobada.visibility = View.VISIBLE
                    textViewCantidadAprobada.text =
                        String.format("Cantidad aprobada : %s", m.cantidadOk)
                }
                textViewCodigo.text = m.codigoMaterial
                textViewDescripcion.text = m.descripcion
                textViewGuiaSalida.text = String.format("Guia : %s", m.guiaSalida)
                if (m.nroSerie.isNotEmpty()) {
                    textViewNroSerie.visibility = View.VISIBLE
                    textViewNroSerie.text = String.format("Nro Medidor : %s", m.nroSerie)
                }
                textViewCantidad.text = m.cantidadMovil.toString()
                buttonEdit.setOnClickListener { view ->
                    listener.onItemClick(
                        m,
                        view,
                        adapterPosition
                    )
                }
                buttonDelete.setOnClickListener { view ->
                    listener.onItemClick(
                        m, view, adapterPosition
                    )
                }
                itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
            }
    }
}