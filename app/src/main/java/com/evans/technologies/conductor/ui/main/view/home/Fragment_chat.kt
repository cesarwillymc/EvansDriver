package com.evans.technologies.conductor.ui.main.view.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.evans.technologies.conductor.ui.main.view.MainActivity
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.model.chats
import com.evans.technologies.conductor.model.notification.notification
import com.evans.technologies.conductor.utils.Adapters.adapter_rv_chat
import com.evans.technologies.conductor.utils.ComunicacionesRealTime.ComunicateFrag.chat
import com.evans.technologies.conductor.utils.ComunicacionesRealTime.OnclickItemListener
import com.evans.technologies.conductor.utils.getDriverId_Prefs
import com.evans.technologies.conductor.utils.getUserName
import com.evans.technologies.conductor.utils.setClaseActual
import com.evans.technologies.conductor.utils.toastLong
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_fragment_chat.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_chat : Fragment(),
    View.OnClickListener {
    private var adapterRview: RecyclerView.Adapter<*>? = null
    var prefs: SharedPreferences? = null
    var dataDriver: SharedPreferences? = null
    var receiver: BroadcastReceiver? = null
    var mensajes = ArrayList<chats>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs =
            context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        dataDriver =
            activity!!.getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        setClaseActual(prefs!!, Fragment_chat().toString())
        ffc_btn_enviar_msg!!.setOnClickListener(this)
        ffc_rv_lista_chats!!.layoutManager = LinearLayoutManager(context)
        //ffc_rv_lista_chats.smoothScrollToPosition(adapterRview.getItemCount());
        adapterRview = adapter_rv_chat(
            context,
            R.layout.dialog_layout_rv_chat_user,
            R.layout.dialog_layout_rv_chat_other,
            mensajes,
            object : OnclickItemListener {
                override fun itemClickNotify(
                    notifica: notification,
                    adapterPosition: Int
                ) {
                }

                override fun itemClickChat(chat: chats, adapterPosition: Int) {}
            })
        ffc_rv_lista_chats!!.adapter = adapterRview
        (Objects.requireNonNull(activity) as MainActivity).updateComuChat(chat { dataSnapshot ->
            llenarData(
                dataSnapshot
            )
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fragment_chat, container, false)

    }

    private fun llenarData(dataSnapshot: DataSnapshot?) {
        if (dataSnapshot != null) {
            mensajes.clear()
            for (dato in dataSnapshot.children) {
                val android = dato.getValue(chats::class.java)!!
                mensajes.add(android)
            }
            adapterRview!!.notifyDataSetChanged()
            try {
                ffc_rv_lista_chats!!.smoothScrollToPosition(adapterRview!!.itemCount - 1)
            } catch (e: Exception) {
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ffc_btn_enviar_msg -> if (!ffc_edit_txt_mensaje_send!!.text.toString().isEmpty()) {
                try {
                    val view = activity!!.currentFocus
                    view!!.clearFocus()
                    val imm =
                        activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                } catch (e: Exception) {
                }
                val send = ffc_edit_txt_mensaje_send!!.text.toString().trim { it <= ' ' }
                val id = getDriverId_Prefs(prefs!!)
                val nombre = getUserName(prefs!!)
                val date = Date()
                val hourFormat: DateFormat =
                    SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val map: MutableMap<String, Any?> =
                    HashMap()
                map["mensajeChat"] = send
                map["nombreChat"] = nombre
                map["idUserChat"] = id
                map["fechaChat"] = hourFormat.format(date)
                FirebaseDatabase.getInstance().reference.child("chatsFirebase")
                    .child(getDriverId_Prefs(prefs!!)!!).push().setValue(map)
                ffc_edit_txt_mensaje_send!!.setText("")

            } else {
                activity!!.toastLong("No se puede enviar mensajes vacios")
            }
        }
    }
}