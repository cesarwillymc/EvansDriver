package com.evans.technologies.conductor.fragments.change_password

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.Retrofit.RetrofitClient
import com.evans.technologies.conductor.Utils.getIDrecuperar
import com.evans.technologies.conductor.Utils.gettokenrecuperar
import com.evans.technologies.conductor.Utils.toastLong
import com.evans.technologies.conductor.fragments.auth.LoginFragment
import com.evans.technologies.conductor.model.Driver
import kotlinx.android.synthetic.main.fragment_changepassword.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class changepassword : Fragment() {
    lateinit var navFragment: SharedPreferences

    var p1: String? = null
    var p2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_changepassword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navFragment =
            context!!.getSharedPreferences("navFragment", Context.MODE_PRIVATE)

        fcp_btn_confirm!!.setOnClickListener {
            p1 = fcp_edtxt_pw1!!.text.toString().trim { it <= ' ' }
            p2 = fcp_edtxt_pw2!!.text.toString().trim { it <= ' ' }
            if (comprobarcampos()) {
                progressBar_pass!!.visibility = View.GONE
                val sendCorreo =
                    RetrofitClient.getInstance().api.sendContraseña_recuperar(
                        getIDrecuperar(navFragment)!!,
                        gettokenrecuperar(navFragment)!!,
                        p1!!
                    )
                sendCorreo.enqueue(object :
                    Callback<Driver?> {
                    override fun onResponse(
                        call: Call<Driver?>,
                        response: Response<Driver?>
                    ) {
                        Log.e("change_set", response.code().toString() + "")
                        if (response.isSuccessful) {
                            progressBar_pass!!.visibility = View.VISIBLE
                            activity!!.toastLong("La contraseña se cambio")
                            navFragment.edit().clear().apply()
                            findNavController().navigate(R.id.action_changepassword_to_loginFragment)
                        } else {
                            progressBar_pass!!.visibility = View.VISIBLE
                            activity!!.toast("Contraseña no valida")
                        }
                    }

                    override fun onFailure(
                        call: Call<Driver?>,
                        t: Throwable
                    ) {
                        progressBar_pass!!.visibility = View.VISIBLE
                    }
                })
            }
        }
    }
    private fun comprobarcampos(): Boolean {
        if (p1!!.isEmpty() || p2!!.isEmpty()) {
            fcp_edtxt_pw1!!.error = "Los campos estan vacios"
            fcp_edtxt_pw2!!.error = "Los campos estan vacios"
            return false
        }
        if (p1!!.length < 6) {
            fcp_edtxt_pw1!!.error = "La contraseña no puede ser menor a 6 digitos"
            return false
        }
        if (p2!!.length < 6) {
            fcp_edtxt_pw2!!.error = "La contraseña no puede ser menor a 6 digitos"
            return false
        }
        if (p1 != p2) {
            fcp_edtxt_pw1!!.error = "Las contraseñas no coinciden"
            fcp_edtxt_pw2!!.error = "Las contraseñas no coinciden"
            return false
        }
        return true
    }
}