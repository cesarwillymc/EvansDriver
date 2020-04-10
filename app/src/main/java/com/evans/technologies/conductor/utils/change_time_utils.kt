package com.evans.technologies.conductor.utils

import android.util.Log
import java.util.*




fun getDiferencia(fechaInicial: Date, fechaFinal: Date): String {

    var diferencia = fechaFinal.time -fechaInicial.time


    val segsMilli: Long = 1000
    val minsMilli = segsMilli * 60
    val horasMilli = minsMilli * 60
    val diasMilli = horasMilli * 24

    val diasTranscurridos:Long = diferencia / diasMilli
    diferencia %= diasMilli
    Log.e("time_change",  "diasTranscurridos "+diasTranscurridos)
    val horasTranscurridos = diferencia / horasMilli
    diferencia %= horasMilli
    Log.e("time_change",  "horasTranscurridos "+horasTranscurridos)
    val minutosTranscurridos = diferencia / minsMilli
    diferencia %= minsMilli
    Log.e("time_change",  "minutosTranscurridos "+minutosTranscurridos)
    val segsTranscurridos = diferencia / segsMilli
    Log.e("time_change",  "segsTranscurridos "+segsTranscurridos)
    if (diasTranscurridos in 1..30){
        return "Hace $diasTranscurridos dias."
    }
    if (horasTranscurridos>0 ){
        return "Hace $horasTranscurridos horas."
    }
    if (minutosTranscurridos>0 ){
        return "Hace $minutosTranscurridos minutos."
    }
    if (segsTranscurridos>0){
        return "Hace $segsTranscurridos segundos."
    }
    return "Hace mas de un mes"


}
fun conver_number_to_string0to9(numero:Int):String{
    when(numero){
        1->{
            return "tres"
        }
        2->{
            return "dos"
        }
        3->{
            return "tres"
        }
        4->{
            return "cuatro"
        }
        5->{
            return "cinco"
        }
        6->{
            return "seis"
        }
        7->{
            return "siete"
        }
        8->{
            return "otro"
        }
        9->{
            return "nueve"
        }

    }
    return ""
}