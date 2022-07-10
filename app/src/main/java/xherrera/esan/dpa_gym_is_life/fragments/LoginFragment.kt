package xherrera.esan.dpa_gym_is_life.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xherrera.esan.dpa_gym_is_life.R
import xherrera.esan.dpa_gym_is_life.viewmodels.LoginViewModel


class LoginFragment : Fragment() {

    private val model: LoginViewModel by viewModels()
    private lateinit var loginView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginView = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton: Button = loginView.findViewById(R.id.btnIngresar)
        val registerButton: Button = loginView.findViewById(R.id.btnRegistrar)
        val etDNI: EditText = loginView.findViewById(R.id.etDNI)
        val etClave: TextView = loginView.findViewById(R.id.etClave)

        var dni = etDNI.text.toString()
        var pass = etClave.text.toString()

        loginButton.setOnClickListener {
            loginButtonListener(dni, pass)
        }

        registerButton.setOnClickListener {
            registerButtonListener()
        }

        readFromFirestore()

        return loginView
    }

    private fun readFromFirestore(){
        val gymDb = Firebase.firestore
        gymDb.collection("gymapp")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun registerButtonListener(){
        findNavController()
            .navigate(LoginFragmentDirections
                .actionLoginFragmentToCustomerRegisterFragment())
    }

    private fun loginButtonListener(dni: String, pass: String){
        if (TextUtils.isEmpty(dni)) {
            Toast.makeText(
                this.context, "Debe ingresar su DNI",
                Toast.LENGTH_LONG
            ).show()
        } else if (dni.length <= 7) {
            Toast.makeText(
                this.context, "DNI no puede tener solo " + dni.length + " dígito(s)",
                Toast.LENGTH_LONG
            ).show()
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(
                this.context, "Debe ingresar su clave",
                Toast.LENGTH_LONG
            ).show()
        } else {
            findNavController()
                .navigate(LoginFragmentDirections
                    .actionLoginFragmentToMenuFragment())
        }
    }

}