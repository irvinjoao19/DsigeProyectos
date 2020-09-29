package com.dsige.dsigeproyectos.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsige.dsigeproyectos.R
import com.dsige.dsigeproyectos.data.local.model.logistica.Pedido
import com.dsige.dsigeproyectos.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_pedido.view.*

class PedidoAdapter(var listener: OnItemClickListener.PedidoListener) :
    RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    private var parte = emptyList<Pedido>()

    fun addItems(list: List<Pedido>) {
        parte = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_pedido, parent, false)
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
            textView1.text = p.nombreTipoPedido
            textView2.text = p.nombreEmpleado
            textView3.text = p.centroCostos
            textView4.text = String.format("Tot. IGV %s", p.cantidad)
            textView5.text = p.estado
            textView6.text = p.fechaEnvio
            textView7.text = p.moneda
            itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
        }
    }
}