package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroSolicitudMaterial
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_materiales.view.*

class SolicitudMaterialRegistroAdapter(var listener: OnItemClickListener.SolicitudRegistroMaterialListener) :
    RecyclerView.Adapter<SolicitudMaterialRegistroAdapter.ViewHolder>() {
    private var materialRegistro = emptyList<RegistroSolicitudMaterial>()

    fun addItems(list: List<RegistroSolicitudMaterial>) {
        materialRegistro = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_materiales, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(materialRegistro[position], listener)
    }

    override fun getItemCount(): Int {
        return materialRegistro.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: RegistroSolicitudMaterial, listener: OnItemClickListener.SolicitudRegistroMaterialListener) =
            with(itemView) {
                if (m.estado != "105") {
                    buttonEdit.visibility = View.GONE
                    buttonDelete.visibility = View.GONE
                    textViewCantidadAprobada.visibility = View.VISIBLE
                }
                when (m.tipoSolicitudId) {
                    1 -> {
                        cardViewMateriales.setCardBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorPrimary
                            )
                        )
                        buttonEdit.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                        buttonDelete.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                    }
                    2 -> {
                        cardViewMateriales.setCardBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.colorAccent
                            )
                        )
                    }
                }

                textViewCodigo.text = m.codigoMaterial
                textViewDescripcion.text = m.descripcion
                textViewGuiaSalida.text = m.guiaSalida
                textViewCantidad.text = m.cantidadMovil.toString()
                textViewCantidadAprobada.text = String.format("Cantidad aprobada : %s", m.cantidadAprobada)
                buttonEdit.setOnClickListener { view ->
                    listener.onItemClick(m, view, adapterPosition)
                }
                buttonDelete.setOnClickListener { view ->
                    listener.onItemClick(m, view, adapterPosition)
                }
                itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
            }
    }
}