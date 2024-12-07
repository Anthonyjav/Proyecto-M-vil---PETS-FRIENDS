package com.tecsup.proyecto.api

import com.tecsup.proyecto.models.Carrito
import com.tecsup.proyecto.models.Cita
import com.tecsup.proyecto.models.Horario
import com.tecsup.proyecto.models.Mascota
import com.tecsup.proyecto.models.MetodoPago
import com.tecsup.proyecto.models.ProductoCarrito
import com.tecsup.proyecto.models.Servicio
import com.tecsup.proyecto.ui.tienda.ProductoResponse
import com.tecsup.proyecto.models.Usuario
import com.tecsup.proyecto.models.Venta
import com.tecsup.proyecto.models.Veterinario
import com.tecsup.proyecto.ui.tienda.CategoriaProducto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE PRODUCTOS
    //-------------------------------------------------------------------------------

    // ENDPOINT DE OBTENER PRODUCTOS
    @GET("productos") // Asegúrate de que esta ruta coincida con la de tu API
    fun getProductos(): Call<List<ProductoResponse>>

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE CATEGORIAS-PRODUCTOS
    //-------------------------------------------------------------------------------

    @GET("categorias-productos") // Asegúrate de que esta ruta coincida con la de tu API
    fun getCategorias(): Call<List<CategoriaProducto>>


    //-------------------------------------------------------------------------------
    // ENDPOINTS DE USUARIOS
    //-------------------------------------------------------------------------------

    // ENDPOINT DE OBTENER USUARIO
    @GET("usuarios")
    fun getUsuarios(): Call<List<Usuario>> // Endpoint para obtener usuarios

    // ENDPOINT DE OBTENER USUARIO POR ID
    @GET("usuarios/{id}")
    fun getUsuario(@Path("id") id: Int): Call<Usuario> // Obtener usuario por id

//------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE MASCOTAS
    //-------------------------------------------------------------------------------

    // ENDPOINT DE OBTENER MASCOTAS POR ID
    @GET("mascotas/usuario/{id}")
    fun getMascotasPorUsuario(@Path("id") usuarioId: Int): Call<List<Mascota>>

    // ENDPOINT PARA AGREGAR MASCOTAS
    @FormUrlEncoded
    @POST("mascotas/")
    fun agregarMascota(
        @Field("usuario") usuario: Int,
        @Field("nombre") nombre: String,
        @Field("especie") especie: String,
        @Field("raza") raza: String,
        @Field("genero") genero: String,
        @Field("fecha_nacimiento") fechaNacimiento: String,
        @Field("peso") peso: String,
        @Field("altura") altura: String,
        @Field("edad") edad: Int,
        @Field("color") color: String,
        @Field("fotom") fotom: String,
        @Field("observaciones") observaciones: String
    ): Call<Void> // Cambia Void si necesitas una respuesta específica

//------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE CITAS
    //-------------------------------------------------------------------------------

    // ENDPOINTS DE OBTENER CITAS POR USUARIO
    @GET("citas/usuario/{usuarioId}/")
    fun getCitasPorUsuario(@Path("usuarioId") usuarioId: Int): Call<List<Cita>>

    @POST("citas/")
    fun crearCita(@Body cita: Cita): Call<Cita>

    @GET("horarios/")
    fun getHorarios(): Call<List<Horario>>
//------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE VETERINARIOS
    //-------------------------------------------------------------------------------

    @GET("ventas/usuario/{id}/")
    fun getVentasPorUsuario(@Path("id") id: Int): Call<List<Venta>>

    @GET("metodos-pago/")
    fun getMetodosPago(): Call<List<MetodoPago>>


//------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE VETERINARIOS
    //-------------------------------------------------------------------------------

    // ENDPOINT PARA OBTENER VETERINARIOS
    @GET("veterinarios")
    fun getVeterinarios(): Call<List<Veterinario>>

//------------------------------------------------------------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // ENDPOINTS DE SERVICIOS
    //-------------------------------------------------------------------------------

    // ENDPOINT PARA OBTENER SERVICIOS
    @GET("servicios")
    fun getServicios(): Call<List<Servicio>>


//------------------------------------------------------------------------------------------------------------------------------------------------



    // MODIFICACIONES PARA EL CARRITO
    //-------------------------------------------------------------------------------
    @POST("carritos/")
    fun crearCarrito(@Body carrito: Carrito): Call<Carrito>
    @GET("carritos/{usuarioId}")
    fun obtenerCarrito(@Path("usuarioId") usuarioId: Int): Call<Carrito>

    // Agregar un producto al carrito
    @POST("productos-carrito/")
    fun agregarProductoAlCarrito(@Body productoCarrito: ProductoCarrito): Call<ProductoCarrito>

    // Obtener productos del carrito
    @GET("productos-carrito/{carritoId}")
    fun getProductosDelCarrito(@Path("carritoId") carritoId: Int): Call<List<ProductoCarrito>>

    @DELETE("productos-carrito/{id}")
    fun eliminarProductoDelCarrito(@Path("id") id: Int): Call<Void>

    // Endpoint de ventas (realizar pago)
    @POST("ventas/")
    fun realizarVenta(@Body venta: Venta): Call<Venta>
}