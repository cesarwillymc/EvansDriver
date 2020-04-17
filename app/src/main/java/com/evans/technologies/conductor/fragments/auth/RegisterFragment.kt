package com.evans.technologies.conductor.fragments.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.Retrofit.RetrofitClient
import com.evans.technologies.conductor.Utils.getCorreoNavFragment
import com.evans.technologies.conductor.model.RegisterResponse
import com.evans.technologies.conductor.Utils.toastLong
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var navFragment: SharedPreferences
    lateinit var code:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navFragment = activity!!.getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        txt_view_terminos.setText(
            Html.fromHtml("Al continuar, confirmo que lei y acepto los " +
                    "<a href='www.google.com.pe'>Terminos y condiciones</a>"+" y la "+"<a href='www.google.com.pe'>Politica de privacidad</a>"))
        txt_view_terminos.setOnClickListener(this)
        register_button_registrar.setOnClickListener(this@RegisterFragment)
        register_edit_text_correo.setText(getCorreoNavFragment(navFragment)!!.toString())
    }


    ////registro de usuarios condiciones

    private fun userSignUp() {

        val name = register_edit_text_nombre.text.toString().trim()
        val surname = register_edit_text_apellido.text.toString().trim()
        val numDocument = register_edit_text_dni.text.toString().trim()
        val email = register_edit_text_correo.text.toString().trim()
        val cellphone = register_edit_text_celular.text.toString().trim()
        val city = register_edit_text_ciudad.text.toString().trim()
        val password = register_edit_text_contraseña.text.toString().trim()
        val passwordconfirm = register_edit_text_verificar_contraseña.text.toString().trim()
        code=register_code_referencial.text.toString().trim()

        if (name.isEmpty())
        {
            register_edit_text_nombre.error = "Es necesario ingresar tu nombre."
            register_edit_text_nombre.requestFocus()
            return
        }
        if (surname.isEmpty())
        {
            register_edit_text_apellido.error = "Es necesario ingresar tus Apellidos."
            register_edit_text_apellido.requestFocus()
            return
        }
        if (numDocument.isEmpty())
        {
            register_edit_text_dni.error = "Ingresa tu número de DNI."
            register_edit_text_dni.requestFocus()
            return
        }
        if (email.isEmpty())
        {
            register_edit_text_correo.error = "Es necesario tu Email."
            register_edit_text_correo.requestFocus()
            return
        }
        if (cellphone.isEmpty())
        {
            register_edit_text_celular.error = "Es necesario tu número de celular"
            register_edit_text_celular.requestFocus()
        }
        if (city.isEmpty())
        {
            register_edit_text_ciudad.error = "Ingresa tu ciudad"
            register_edit_text_ciudad.requestFocus()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            register_edit_text_correo.error = "Email invalido"
            register_edit_text_correo.requestFocus()
            return
        }
        if (password.isEmpty())
        {
            register_edit_text_contraseña.error = "Ingresa una contraseña."
            register_edit_text_contraseña.requestFocus()
            return
        }
        if (password.length <= 8)
        {
            register_edit_text_contraseña.error = "La contraseña debe de tener al menos 8 caracteres"
            register_edit_text_contraseña.requestFocus()
            return
        }
        if (passwordconfirm.length<=8 || !(passwordconfirm.equals(password)))
        {
            register_edit_text_verificar_contraseña.error = "Contraseñas son diferentes."
            register_edit_text_verificar_contraseña.requestFocus()
            return
        }
        register_progressbar.visibility= View.VISIBLE
        register_button_registrar.isEnabled=false
        val call = RetrofitClient
            .getInstance()
            .api
            .createUser(name, surname, numDocument, email, cellphone, city, password, passwordconfirm)

        call.enqueue(object: Callback<RegisterResponse> {

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                when(response.code()){
                    200->{
                        register_progressbar.visibility= View.GONE
                        actions()
                        activity!!.toastLong("Se registro exitosamente")
                    }
                    400->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("Contraseña no coincide")
                    }
                    401->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("El email ya esta en uso")
                    }
                    402->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("El nombre no debe exeder de los 40 caracteres")
                    }
                    403->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("Los apellidos no debe exeder de los 40 caracteres")
                    }
                    404->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("E-mail no debe exeder de los 40 caracteres o no es un e-mail valido")
                    }
                    405->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("El numero celular son solo digitos")
                    }
                    406->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("El numero celular son solo digitos")
                    }
                    407->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("El numero dni son solo digitos")
                    }
                    500->{
                        register_progressbar.visibility= View.GONE
                        activity!!.toastLong("Error al registrarse")
                    }

                }
                register_button_registrar.isEnabled=true

            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                activity!!.toastLong("Revise su conexion a internet")
                register_progressbar.visibility= View.GONE
                register_button_registrar.isEnabled=true

            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.register_button_registrar-> {
                try{
                    var view = activity!!.currentFocus
                    view!!.clearFocus()
                    if (view != null) {
                        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    }
                }catch (e:Exception){

                }
                userSignUp()
            }
            R.id.txt_view_terminos->{
                val url = "http://www.proevans.com/es/general-terms-of-use"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

    }

    private fun actions(){
        if (code.isEmpty()){
            findNavController().navigate(R.id.action_registerFragment_to_inicioFragment)
        }else{
            val statusTrip= RetrofitClient.getInstance().api.putReferido(getCorreoNavFragment(navFragment)!!.toString(),code)
            statusTrip.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if ( response.isSuccessful){
                        Toast.makeText(context,"Refirio correctamente..",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context,"No se pudo referir, revise su conexion ${response.code()}",Toast.LENGTH_LONG).show()
                    }
                    findNavController().navigate(R.id.action_registerFragment_to_inicioFragment)

                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("register", t.message +"  "+id+"  "+code)
                    Toast.makeText(context,"No se pudo referir, revise su conexion",Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_inicioFragment)
                }

            })
        }
    }
}
