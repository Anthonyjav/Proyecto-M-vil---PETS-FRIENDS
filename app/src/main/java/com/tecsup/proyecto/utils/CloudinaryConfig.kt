package com.tecsup.proyecto.utils

import com.cloudinary.android.MediaManager
import com.tecsup.proyecto.MyApplication

object CloudinaryConfig {

    // Flag para verificar si ya fue inicializado
    private var isInitialized = false

    fun initCloudinary() {
        if (!isInitialized) {
            val config: MutableMap<String, String> = HashMap()
            config["cloud_name"] = "dq2suwtlm" // Reemplaza con tu Cloud Name
            config["api_key"] = "114247465137567"       // Reemplaza con tu API Key
            config["api_secret"] = "sirr5OuyYRfaNcd5Q7guSSfS_GE" // Reemplaza con tu API Secret
            MediaManager.init(MyApplication.context, config)
            isInitialized = true // Marca como inicializado
        }
    }
}