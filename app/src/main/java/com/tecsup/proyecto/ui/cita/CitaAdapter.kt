package com.tecsup.proyecto.ui.cita

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.Cita
import com.tecsup.proyecto.models.CitaDetallada

class CitaAdapter(private val citas: List<CitaDetallada>) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvServicio: TextView = itemView.findViewById(R.id.tvServicio)
        val tvMascota: TextView = itemView.findViewById(R.id.tvMascota)
        val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]
        holder.tvServicio.text = "Servicio: ${cita.nombreServicio}"  // Mostrar ID de servicio, puedes hacer más llamadas para mostrar el nombre
        holder.tvMascota.text = "Mascota: ${cita.nombreMascota}"    // Mostrar ID de mascota
        holder.tvHorario.text = "Horario: ${cita.horario}"    // Mostrar ID de horario
        holder.tvFecha.text = "Fecha: ${cita.fecha_cita}"
        holder.tvEstado.text = "Estado: ${if (cita.estado) "Hecha" else "Pendiente"}"
    }

    override fun getItemCount(): Int = citas.size
}
