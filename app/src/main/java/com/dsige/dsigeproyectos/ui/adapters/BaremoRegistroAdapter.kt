package com.dsige.dsigeproyectos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.engie.RegistroBaremo
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_baremo.view.*

class BaremoRegistroAdapter(private var listener: OnItemClickListener.RegistroBaremoListener) :
    RecyclerView.Adapter<BaremoRegistroAdapter.ViewHolder>() {
    private var baremoRegistros = emptyList<RegistroBaremo>()

    fun addItems(list: List<RegistroBaremo>) {
        baremoRegistros = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_baremo, parent, false)
        return ViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(baremoRegistros[position], listener)
    }

    override fun getItemCount(): Int {
        return baremoRegistros.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(b: RegistroBaremo, listener: OnItemClickListener.RegistroBaremoListener) = with(itemView) {
            if (b.estado == "121") {
                buttonEdit.visibility = View.GONE
                buttonDelete.visibility = View.GONE
                textViewCantidadAprobada.visibility = View.VISIBLE
                textViewCantidadAprobada.text = String.format("Cantidad aprobada : %s", b.cantidadOk)
            }
            textViewCodigo.text = b.codigoBaremo
            textViewDescripcion.text = b.descripcion
            textViewCantidad.text = b.cantidadMovil.toString()
            buttonEdit.setOnClickListener { view ->
                listener.onItemClick(b, view, adapterPosition)
            }
            buttonDelete.setOnClickListener { view ->
                listener.onItemClick(b, view, adapterPosition)
            }
        }
    }
}