package com.tecsup.proyecto.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.tecsup.proyecto.R

class QRDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme) // Aplica el tema de fondo desenfocado
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation // Aplica las animaciones
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del dialog
        return inflater.inflate(R.layout.dialog_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar el código QR en el ImageView
        val qrImageView: ImageView = view.findViewById(R.id.qrImageView)
        // Aquí puedes establecer la imagen del código QR (ejemplo con un recurso)
        qrImageView.setImageResource(R.drawable.ic_qr) // Cambia esto por tu código QR

        // Configurar el botón de cerrar
        val closeButton: Button = view.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dismiss() // Cierra el dialog
        }
    }
}
