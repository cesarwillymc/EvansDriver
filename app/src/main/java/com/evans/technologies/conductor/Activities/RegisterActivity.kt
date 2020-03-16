package com.evans.technologies.conductor.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.evans.technologies.conductor.R
import com.evans.technologies.conductor.Retrofit.RetrofitClient
import com.evans.technologies.conductor.model.RegisterResponse
import com.evans.technologies.conductor.Utils.toastLong
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txt_view_terminos.setText(
            Html.fromHtml("Al continuar, confirmo que lei y acepto los " +
                    "<a href='www.google.com.pe'>Terminos y condiciones</a>"+" y la "+"<a href='www.google.com.pe'>Politica de privacidad</a>"))
        txt_view_terminos.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        register_image_view_atras.setOnClickListener(this@RegisterActivity)
        register_button_registrar.setOnClickListener(this@RegisterActivity)

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
                        toastLong("Se registro exitosamente")
                    }
                    400->{
                        register_progressbar.visibility= View.GONE
                        toastLong("Contraseña no coincide")
                    }
                    401->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El email ya esta en uso")
                    }
                    402->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El nombre no debe exeder de los 40 caracteres")
                    }
                    403->{
                        register_progressbar.visibility= View.GONE
                        toastLong("Los apellidos no debe exeder de los 40 caracteres")
                    }
                    404->{
                        register_progressbar.visibility= View.GONE
                        toastLong("E-mail no debe exeder de los 40 caracteres o no es un e-mail valido")
                    }
                    405->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El numero celular son solo digitos")
                    }
                    406->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El numero celular son solo digitos")
                    }
                    407->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El numero dni son solo digitos")
                    }
                    500->{
                        register_progressbar.visibility= View.GONE
                        toastLong("Error al registrarse")
                    }

                }
                register_button_registrar.isEnabled=true

            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                toastLong("Revise su conexion a internet")
                register_progressbar.visibility= View.GONE
                register_button_registrar.isEnabled=true

            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.register_button_registrar-> {
                try{
                    var view = this.getCurrentFocus()
                    view!!.clearFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                    }
                }catch (e:Exception){

                }
                userSignUp()
            }
            R.id.register_image_view_atras -> onBackPressed()
            R.id.txt_view_terminos->{
                val url = "http://www.proevans.com/es/general-terms-of-use"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

    }

    private fun actions(){
        register_button_registrar.isEnabled=true
        finish()
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
}
