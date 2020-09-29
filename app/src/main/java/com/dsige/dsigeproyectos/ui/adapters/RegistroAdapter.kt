package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.trinidad.Registro
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_registro.view.*

class RegistroAdapter(private var listener: OnItemClickListener.RegistroListener) :
    RecyclerView.Adapter<RegistroAdapter.ViewHolder>() {

    private var registros = emptyList<Registro>()

    fun addItems(list: List<Registro>) {
        registros = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(registros[position], listener)
    }

    override fun getItemCount(): Int {
        return registros.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_registro, parent, false)
        return ViewHolder(v!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(r: Registro, listener: OnItemClickListener.RegistroListener) =
            with(itemView) {
                textViewObra.text = String.format("Obra : %s", r.nroObra)
                textViewNroPoste.text = String.format("Nro Poste : %s", r.nroPoste)
                textViewEstado.text = when (r.active) {
                    1 -> "Listo para enviar"
                    2 -> "Enviado"
                    else -> "Cerrar Registro"
                }
                textViewEstado.setTextColor(
                    when (r.active) {
                        1 -> ContextCompat.getColor(itemView.context, R.color.colorGreen)
                        2 -> ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                        else -> ContextCompat.getColor(itemView.context, R.color.colorRed)
                    }
                )
                if (r.tipo == 2) {
                    textViewFecha.text = r.fecha
                    textViewFecha.visibility = View.VISIBLE
                } else
                    textViewFecha.visibility = View.GONE

                itemView.setOnClickListener { v -> listener.onItemClick(r, v, adapterPosition) }
            }
    }
}