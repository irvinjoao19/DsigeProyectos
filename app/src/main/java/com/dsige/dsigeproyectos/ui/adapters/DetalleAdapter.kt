package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.helper.Util
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.trinidad.RegistroDetalle
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_registro_detalle.view.*

class DetalleAdapter(private var listener: OnItemClickListener.DetalleListener) :
    RecyclerView.Adapter<DetalleAdapter.ViewHolder>() {

    private var registros = emptyList<RegistroDetalle>()

    fun addItems(list: List<RegistroDetalle>) {
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
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_registro_detalle, parent, false)
        return ViewHolder(v!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(r: RegistroDetalle, listener: OnItemClickListener.DetalleListener) =
            with(itemView) {
                if (r.active2 == 1 && r.active1 == 1) {
                    textViewStatus.text = String.format("%s", "Cerrado")
                    textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }else{
                    textViewStatus.text = String.format("%s", "Por Completar")
                    textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }

                textViewPunto.text = r.nombrePunto
                textViewFecha.text = Util.getFecha()
                buttonAntes.setOnClickListener { v -> listener.onItemClick(r, v, adapterPosition) }
                buttonDespues.setOnClickListener { v ->
                    listener.onItemClick(
                        r,
                        v,
                        adapterPosition
                    )
                }
            }
    }
}