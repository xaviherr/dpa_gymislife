package dev.mvgioser.registroymembresia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val etDNI = findViewById<EditText>(R.id.etDNI)
        val etClave: TextView = findViewById(R.id.etClave)
        val btnIngresar: Button = findViewById(R.id.btnIngresar)
        val btnRegistrar: Button = findViewById(R.id.btnRegistrar)

        btnIngresar.setOnClickListener {

            var dni: String = etDNI.text.toString()
            var clave: String = etClave.text.toString()

            if (TextUtils.isEmpty(dni)) {
                Toast.makeText(
                    this, "Debe ingresar su DNI",
                    Toast.LENGTH_LONG
                ).show()
            } else if (etDNI.text.length <= 7) {
                Toast.makeText(
                    this, "DNI no puede tener solo " + etDNI.text.length + " dígito(s)",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (TextUtils.isEmpty(clave)) {
                    Toast.makeText(
                        this, "Debe ingresar su clave",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    val rootRef = FirebaseFirestore.getInstance()
                    val loginRef =
                        rootRef.collection("registro").document(etDNI.text.toString())
                    loginRef.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dnilogin = task.result
                            if (dnilogin.exists()) {
                                val clavelogin = dnilogin.getString("clave").toString()
                                if (clavelogin == etClave.text.toString()) {
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                                else{
                                    Toast.makeText(
                                        this, "Clave incorrecta",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }
    }
}


