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
import com.evans.technologies.conductor.Utils.*
import kotlinx.android.synthetic.main.fragment_tus_viajes_gratis.*

/**
 * A simple [Fragment] subclass.
 */
class TusViajesGratisFragment : Fragment()  {
    lateinit var vista:View
    private lateinit  var prefs:SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vista= inflater.inflate(R.layout.fragment_tus_viajes_gratis, container, false)
        prefs = context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setClaseActual(prefs!!!!, TusViajesGratisFragment().toString())


        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ftvg_text_name_driver.text= getUserName(prefs)+" "+ getUserSurname(prefs)
        ftvg_txt_correo_driver.text= getUserEmail(prefs)
        ftvg_txt_nombre_driver.text= getUserName(prefs)
        ftvg_txt_ciudad_driver.text= getCityDriver(prefs)+", Perú"
        ftvg_txt_vehiculo_driver.text= getmodelCar(prefs) +" "+ getcolorCar(prefs)
        ftvg_text_placa_driver.text= getlicenseCar(prefs)
        if(!getRutaImagen(prefs).equals("nulo")){
            val myBitmap = BitmapFactory.decodeFile(getRutaImagen(prefs))
            Glide.with(context!!).asBitmap().load(getImageRotate(getRutaImagen(prefs)!!,myBitmap))
                .apply(RequestOptions.circleCropTransform())
                .into(ftvg_driver_imagen_perfil)
        }else{
            Log.e("img",getImgUrlProfile(prefs))
//            Glide.with(context!!).load(getImgUrlProfile(prefs))
//                .apply(RequestOptions.circleCropTransform())
//                .into(ftvg_driver_imagen_perfil)
            Glide.with(view).load(getImgUrlProfile(prefs)).apply(RequestOptions.circleCropTransform()).into(ftvg_driver_imagen_perfil)
                .onLoadFailed(view.resources.getDrawable(R.drawable.profile))

        }


    }



}
