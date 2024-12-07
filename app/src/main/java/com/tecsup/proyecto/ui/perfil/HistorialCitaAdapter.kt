package com.tecsup.proyecto.ui.perfil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tecsup.proyecto.R
import com.tecsup.proyecto.models.CitaDetallada

class HistorialCitaAdapter(private val citas: List<CitaDetallada>) : RecyclerView.Adapter<HistorialCitaAdapter.HistorialCitaViewHolder>() {

    class HistorialCitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvServicio: TextView = itemView.findViewById(R.id.tvHistorialServicio)
        val tvMascota: TextView = itemView.findViewById(R.id.tvHistorialMascota)
        val tvFecha: TextView = itemView.findViewById(R.id.tvHistorialFecha)
        val tvHorario: TextView = itemView.findViewById(R.id.tvHistorialHorario)
        val tvEstado: TextView = itemView.findViewById(R.id.tvHistorialEstado) // Nuevo estado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialCitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial_citas, parent, false)
        return HistorialCitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialCitaViewHolder, position: Int) {
        val cita = citas[position]
        holder.tvServicio.text = "Servicio: ${cita.nombreServicio}"
        holder.tvMascota.text = "Mascota: ${cita.nombreMascota}"
        holder.tvFecha.text = "Fecha: ${cita.fecha_cita}"
        holder.tvHorario.text = "Horario: ${cita.horario}"
        holder.tvEstado.text = "Estado: Completado" // Asignar estado
    }

    override fun getItemCount(): Int = citas.size
}