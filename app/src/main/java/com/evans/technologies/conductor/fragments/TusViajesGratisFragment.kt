package com.evans.technologies.conductor.fragments


import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.utils.*

/**
 * A simple [Fragment] subclass.
 */
class TusViajesGratisFragment : Fragment()  {
    lateinit var vista:View
    private var prefs: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vista= inflater.inflate(R.layout.fragment_tus_viajes_gratis, container, false)
        prefs = context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setClaseActual(prefs!!, TusViajesGratisFragment().toString())
        ButterKnife.bind(this,vista)
        cargardatos()

        return vista
    }

    private fun cargardatos() {
        var  dataDriver = getContext()!!.getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        var prefs = getContext()!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        var imagendatos=vista.findViewById<ImageView>(R.id.ftvg_driver_imagen_perfil)
        var ciudadPais=vista.findViewById<TextView>(R.id.ftvg_txt_ciudad_driver)
        var ratingDriver=vista.findViewById<TextView>(R.id.ftvg_driver_txt_rating)
        var nombreApellidos=vista.findViewById<TextView>(R.id.ftvg_text_name_driver)
        var viajesTotales=vista.findViewById<TextView>(R.id.ftvg_text_viajes)
        var correoDriver=vista.findViewById<TextView>(R.id.ftvg_txt_correo_driver)
        var marcaColorVehivulo=vista.findViewById<TextView>(R.id.ftvg_txt_vehiculo_driver)
        var placaVehiculo=vista.findViewById<TextView>(R.id.ftvg_text_placa_driver)
        var nombrePerfil=vista.findViewById<TextView>(R.id.ftvg_txt_nombre_driver)

        //Dar valores a la imagenes
        Log.e("bitmaperror",getRutaImagen(prefs!!))
        if(!getRutaImagen(prefs).equals("nulo")){
            val myBitmap = BitmapFactory.decodeFile(getRutaImagen(prefs))
            Glide.with(context!!).asBitmap().load(getImageRotate(getRutaImagen(prefs!!)!!,myBitmap))
                .apply(RequestOptions.circleCropTransform())
                .into(imagendatos)
        }

       // var bitmap = BitmapFactory.decodeFile(getRutaImagen(prefs))
      //  imagendatos.setImageBitmap(bitmap)
        nombreApellidos.text= getUserName(prefs)+" "+ getUserSurname(prefs)
        correoDriver.text= getUserEmail(prefs)
        nombrePerfil.text= getUserName(prefs)
        ciudadPais.text= getCityDriver(prefs)+", Perú"
        marcaColorVehivulo.text= getmodelCar(prefs) +" "+ getcolorCar(prefs)
        placaVehiculo.text= getlicenseCar(prefs)

    }


}
