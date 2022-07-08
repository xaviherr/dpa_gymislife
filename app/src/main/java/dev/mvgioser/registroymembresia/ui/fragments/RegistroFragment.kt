package dev.mvgioser.registroymembresia.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dev.mvgioser.registroymembresia.LoginActivity
import dev.mvgioser.registroymembresia.R
import dev.mvgioser.registroymembresia.ui.fragments.model.CuentaModel
import java.text.SimpleDateFormat
import java.util.*


open class RegistroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_registro, container, false)
        val etDNI: EditText = view.findViewById(R.id.etDNI)
        val etNombres: EditText = view.findViewById(R.id.etNombres)
        val etApellidos: EditText = view.findViewById(R.id.etApellidos)
        val spnGenero: Spinner = view.findViewById(R.id.spnGenero)
        val etFechaNac: EditText = view.findViewById(R.id.etFechaNac)
        val etCelular: EditText = view.findViewById(R.id.etCelular)
        val etEmail: EditText = view.findViewById(R.id.etEmail)
        val etClave: EditText = view.findViewById(R.id.etClave)
        val etClave2: EditText = view.findViewById(R.id.etClave2)
        val btnRegistrarCuenta: Button = view.findViewById(R.id.btnRegistrarCuenta)
        val db = FirebaseFirestore.getInstance()
        var generoValue = ""
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genero_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spnGenero.adapter = adapter
        }

        spnGenero.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                generoValue = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        fun EditText.transformIntoDatePicker(
            context: Context,
            format: String,
            maxDate: Date? = null
        ) {
            isFocusableInTouchMode = false
            isClickable = true
            isFocusable = false

            val myCalendar = Calendar.getInstance()
            val datePickerOnDataSetListener =
                DatePickerDialog.OnDateSetListener { _, anio, mesdelanio, diadelanio ->
                    myCalendar.set(Calendar.YEAR, anio)
                    myCalendar.set(Calendar.MONTH, mesdelanio)
                    myCalendar.set(Calendar.DAY_OF_MONTH, diadelanio)
                    val sdf = SimpleDateFormat(format, Locale.US)
                    setText(sdf.format(myCalendar.time))
                }

            setOnClickListener {
                DatePickerDialog(
                    context, datePickerOnDataSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).run {
                    maxDate?.time?.also { datePicker.maxDate = it }
                    show()
                }
            }
        }

        etFechaNac.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")
        etFechaNac.transformIntoDatePicker(requireContext(), "dd/MM/yyyy", Date())

        btnRegistrarCuenta.setOnClickListener {
            var dni = etDNI.text.toString()
            var nombres = etNombres.text.toString()
            var apellidos = etApellidos.text.toString()
            var genero = spnGenero.getSelectedItem().toString()
            var fechanac = etFechaNac.text.toString()
            var celular = etCelular.text.toString()
            var email = etEmail.text.toString()
            var clave = etClave.text.toString()
            var clave2 = etClave2.text.toString()

            if (TextUtils.isEmpty(dni)) {
                Snackbar.make(view, "Debe ingresar su DNI", Snackbar.LENGTH_LONG).show();
            } else if (etDNI.text.length <= 7) {
                Snackbar.make(
                    view,
                    "DNI no puede tener solo " + etDNI.text.length + " dígito(s)",
                    Snackbar.LENGTH_LONG
                ).show();
            } else if (etDNI.text.length <= 7) {
                Snackbar.make(
                    view,
                    "DNI no puede tener solo " + etDNI.text.length + " dígito(s)",
                    Snackbar.LENGTH_LONG
                ).show();
            } else if (TextUtils.isEmpty(nombres)) {
                Snackbar.make(view, "Debe ingresar sus nombres", Snackbar.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(apellidos)) {
                Snackbar.make(view, "Debe ingresar sus apellidos", Snackbar.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(fechanac)) {
                Snackbar.make(view, "Debe ingresar su fecha de nacimiento", Snackbar.LENGTH_LONG)
                    .show();
            } else if (TextUtils.isEmpty(celular)) {
                Snackbar.make(view, "Debe ingresar su celular", Snackbar.LENGTH_LONG).show();
            } else if (etCelular.text.length <= 8) {
                Snackbar.make(
                    view,
                    "Celular no puede tener solo " + etDNI.text.length + " dígito(s)",
                    Snackbar.LENGTH_LONG
                ).show();
            } else if (TextUtils.isEmpty(email)) {
                Snackbar.make(view, "Debe ingresar su email", Snackbar.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(clave)) {
                Snackbar.make(view, "Debe ingresar su contraseña", Snackbar.LENGTH_LONG).show();
            } else if (clave2 != clave) {
                Snackbar.make(view, "Contraseñas no coinciden", Snackbar.LENGTH_LONG).show();
            } else {
                val nuevaCuenta = CuentaModel(
                    dni,
                    nombres,
                    apellidos,
                    genero,
                    fechanac,
                    celular,
                    email,
                    clave,
                    clave2
                )
                val newID: String = etDNI.text.toString()
                val rootRef = FirebaseFirestore.getInstance()

                val dniRef = rootRef.collection("registro").document(etDNI.text.toString())
                dniRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            Snackbar.make(view, "DNI ya está registrado", Snackbar.LENGTH_LONG)
                                .show();
                        } else {
                            val dialogClickListener =
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            db.collection("registro")
                                                .document(newID)
                                                .set(nuevaCuenta)
                                                .addOnSuccessListener {
                                                    Snackbar.make(
                                                        view,
                                                        "Se registró la cuenta correctamente",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()
                                                    val intent = Intent(activity, LoginActivity::class.java)
                                                    Handler().postDelayed({
                                                        startActivity(intent)
                                                    }, 3000)
                                                }
                                        }
                                        DialogInterface.BUTTON_NEGATIVE -> {
                                            dialog.dismiss()
                                        }
                                    }
                                }

                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("¿Registrar cuenta?")
                                .setPositiveButton("Sí", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show()
                        }
                    }
                }
            }
        }; return view
    }
}


  /*  val dialogClickListener =
        DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {}
                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }

    val builder = AlertDialog.Builder(context)
    builder.setMessage("Are you sure?")
    .setPositiveButton("Yes", dialogClickListener)
    .setNegativeButton("No", dialogClickListener).show()
}*/