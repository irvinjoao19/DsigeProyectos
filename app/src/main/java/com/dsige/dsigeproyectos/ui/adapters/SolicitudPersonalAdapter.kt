package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.Solicitud
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_personal.view.*

class SolicitudPersonalAdapter(private var listener: OnItemClickListener.SolicitudListener) :
    RecyclerView.Adapter<SolicitudPersonalAdapter.ViewHolder>() {

    private var solicitudes = emptyList<Solicitud>()

    fun addItems(list: List<Solicitud>) {
        solicitudes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_personal, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(solicitudes[position], listener)
    }

    override fun getItemCount(): Int {
        return solicitudes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(s: Solicitud, listener: OnItemClickListener.SolicitudListener) = with(itemView) {
            textViewSolicitud.text = String.format("Nro Solicitud : %s", s.identity)
            textViewObra.text = String.format("Obra/Td : %s", s.obraTd)
            textViewFecha.text = String.format("Fecha Solicitud : %s", s.fechaAtencion)

            textViewFecha.text = s.fechaAtencion
            textViewEstado.text = s.estadoSol
            textViewPersonal.text = s.nombrePersonal

            if (s.tipoSolicitudId == 2) {
                textViewTipoDevolucion.visibility = View.VISIBLE
                textViewTipoDevolucion.text = s.nombreTipoMaterial
            }
            itemView.setOnClickListener { v -> listener.onItemClick(s, v, adapterPosition) }
        }
    }
}