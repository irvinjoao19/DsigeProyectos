package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_pedido_detalle.view.*

class PedidoDetalleAdapter(var listener: OnItemClickListener.PedidoListener) :
    RecyclerView.Adapter<PedidoDetalleAdapter.ViewHolder>() {

    private var parte = emptyList<Pedido>()

    fun addItems(list: List<Pedido>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_pedido_detalle, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(parte[position], listener)
    }

    override fun getItemCount(): Int {
        return parte.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Pedido, listener: OnItemClickListener.PedidoListener) = with(itemView) {
            textView1.text = p.articulo
            textView2.text = p.nombreArticulo
            textView30.text = String.format("%s", p.cantidad)
            textView31.text = String.format("%s", p.cantidadAprobada)
            textView32.text = String.format("%s", p.precio)
            textView33.text = String.format("%s", p.cantidad)
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}