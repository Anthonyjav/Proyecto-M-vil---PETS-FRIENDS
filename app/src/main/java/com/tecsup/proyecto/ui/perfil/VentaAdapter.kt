package com.tecsup.proyecto.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.Venta
import com.tecsup.proyecto.models.VentaDetallada

class VentaAdapter(private val ventas: List<VentaDetallada>) : RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    class VentaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMetodoPago: TextView = itemView.findViewById(R.id.tvMetodoPago)
        val tvCorreo: TextView = itemView.findViewById(R.id.tvCorreo)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaVenta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = ventas[position]
        holder.tvMetodoPago.text = "Método de pago: ${venta.nombreMetodoPago}"
        holder.tvCorreo.text = "Correo: ${venta.correo}"
        holder.tvSubtotal.text = "Subtotal: S/.${venta.subtotal}"
        holder.tvTotal.text = "Total: S/.${venta.total}"
        holder.tvFecha.text = "Fecha: ${venta.fecha_venta}"
    }

    override fun getItemCount(): Int = ventas.size
}
