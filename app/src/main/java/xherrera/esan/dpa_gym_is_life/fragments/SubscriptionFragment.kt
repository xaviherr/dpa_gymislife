package xherrera.esan.dpa_gym_is_life.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import xherrera.esan.dpa_gym_is_life.R
import xherrera.esan.dpa_gym_is_life.data.entities.MembresiaModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class SubscriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_subscription, container, false)
        val etDNI: EditText = view.findViewById(R.id.etDNI)
        val btnValidarCliente: Button = view.findViewById(R.id.btnValidarCliente)
        val tvMembresiaVigente: TextView = view.findViewById(R.id.tvMembresiaVigente)
        val etFechaInicioMembresia: EditText = view.findViewById(R.id.etFechaInicioMembresia)
        val etFechaFinMembresia: EditText = view.findViewById(R.id.etFechaFinMembresia)
        val etCostoMembresia: EditText = view.findViewById(R.id.etCostoMembresia)
        val btnVincularMembresia: Button = view.findViewById(R.id.btnVincularMembresia)
        val db = FirebaseFirestore.getInstance()

        fun getLocalDateFromString(d: String, format: String): LocalDate {
            return LocalDate.parse(d, DateTimeFormatter.ofPattern(format))
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
                    maxDate?.time?.also { datePicker.minDate = it }
                    show()
                }
            }
        }

        etFechaInicioMembresia.transformIntoDatePicker(requireContext(), "dd/MM/yyyy")
        etFechaInicioMembresia.transformIntoDatePicker(requireContext(), "dd/MM/yyyy", Date())

        fun EditText.transformIntoDatePicker2(
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
                    maxDate?.time?.also { datePicker.minDate = it }
                    show()
                }
            }
        }

        etFechaFinMembresia.transformIntoDatePicker2(requireContext(), "dd/MM/yyyy")
        etFechaFinMembresia.transformIntoDatePicker2(requireContext(), "dd/MM/yyyy", Date())

        btnValidarCliente.setOnClickListener {

            var dni = etDNI.text.toString()

            if (TextUtils.isEmpty(dni)) {
                Snackbar.make(view, "Debe ingresar su DNI", Snackbar.LENGTH_LONG).show();
            } else if (etDNI.text.length <= 7) {
                Snackbar.make(
                    view,
                    "DNI no puede tener solo " + etDNI.text.length + " dígito(s)",
                    Snackbar.LENGTH_LONG
                ).show();
            } else {
                tvMembresiaVigente.text = ""
                val rootRef = FirebaseFirestore.getInstance()
                val membresiaRef =
                    rootRef.collection("membresia").document(etDNI.text.toString())
                membresiaRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val dnimembresia = task.result
                        if (dnimembresia.exists()) {

                            val fechafinstr = dnimembresia.getString("fechafin").toString()

                            val sdf = SimpleDateFormat("dd/MM/yyyy")
                            val hoy = sdf.format(Date())

                            fun getDaysDif(fechafinstr: LocalDate, hoy: LocalDate): Long {
                                return ChronoUnit.DAYS.between(fechafinstr, hoy)
                            }

                            val dias = getDaysDif(
                                getLocalDateFromString(hoy, "dd/MM/yyyy"),
                                getLocalDateFromString(fechafinstr, "dd/MM/yyyy")
                            )

                            tvMembresiaVigente.text =
                                "Quedan " + dias + " días hasta " + dnimembresia.getString("fechafin")

                            Snackbar.make(
                                view,
                                "DNI cuenta con membresía vigente",
                                Snackbar.LENGTH_LONG
                            )
                                .show();
                        } else {
                            val dniRef =
                                rootRef.collection("registro").document(etDNI.text.toString())
                            dniRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val dnicuenta = task.result
                                    if (dnicuenta.exists()) {

                                        tvMembresiaVigente.text =
                                            "DNI no cuenta con membresía vigente"

                                        Snackbar.make(
                                            view,
                                            "DNI no cuenta con membresía vigente",
                                            Snackbar.LENGTH_LONG
                                        )
                                            .show();
                                    } else {
                                        Snackbar.make(
                                            view,
                                            "DNI no tiene una cuenta",
                                            Snackbar.LENGTH_LONG
                                        )
                                            .show();
                                        findNavController()
                                            .navigate(SubscriptionFragmentDirections
                                                .actionSubscriptionFragmentToLoginFragment())
                                    }
                                }
                            }
                        }
                    }

                    btnVincularMembresia.setOnClickListener {

                        var fechainicio = etFechaInicioMembresia.text.toString()
                        var fechafin = etFechaFinMembresia.text.toString()
                        var costo = etCostoMembresia.text.toString()

                        if (TextUtils.isEmpty(dni)) {
                            Snackbar.make(
                                view,
                                "Debe ingresar su DNI",
                                Snackbar.LENGTH_LONG
                            ).show();
                        } else if (etDNI.text.length <= 7) {
                            Snackbar.make(
                                view,
                                "DNI no puede tener solo " + etDNI.text.length + " dígito(s)",
                                Snackbar.LENGTH_LONG
                            )
                        } else if (TextUtils.isEmpty(fechainicio)) {
                            Snackbar.make(
                                view,
                                "Debe ingresar fecha de inicio de membresía",
                                Snackbar.LENGTH_LONG
                            ).show();
                        } else if (TextUtils.isEmpty(fechafin)) {
                            Snackbar.make(
                                view,
                                "Debe ingresar fecha de fin de membresía",
                                Snackbar.LENGTH_LONG
                            ).show();
                        } else if (TextUtils.isEmpty(costo)) {
                            Snackbar.make(
                                view,
                                "Debe ingresar costo de membresía",
                                Snackbar.LENGTH_LONG
                            )
                                .show();
                        } else {
                            val nuevaMembresia = MembresiaModel(
                                dni,
                                fechainicio,
                                fechafin,
                                costo
                            )
                            val newID: String = etDNI.text.toString()
                            val rootRef = FirebaseFirestore.getInstance()

                            val dniRef =
                                rootRef.collection("membresia").document(etDNI.text.toString())
                            dniRef.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    if (document.exists()) {
                                        Snackbar.make(
                                            view,
                                            "DNI ya está registrado",
                                            Snackbar.LENGTH_LONG
                                        )
                                            .show();
                                    } else {
                                        val dialogClickListener =
                                            DialogInterface.OnClickListener { dialog, which ->
                                                when (which) {
                                                    DialogInterface.BUTTON_POSITIVE -> {
                                                        db.collection("membresia")
                                                            .document(newID)
                                                            .set(nuevaMembresia)
                                                            .addOnSuccessListener {
                                                                Snackbar.make(
                                                                    view,
                                                                    "Se vinculó la membresía correctamente",
                                                                    Snackbar.LENGTH_LONG
                                                                ).show()
                                                                findNavController()
                                                                    .navigate(SubscriptionFragmentDirections
                                                                        .actionSubscriptionFragmentToLoginFragment())
                                                            }.addOnFailureListener {
                                                                Snackbar.make(
                                                                    view,
                                                                    "Error al vincular la membresía",
                                                                    Snackbar.LENGTH_LONG
                                                                ).show()
                                                            }
                                                    }
                                                    DialogInterface.BUTTON_NEGATIVE -> {
                                                        dialog.dismiss()
                                                    }
                                                }
                                            }
                                        val builder = AlertDialog.Builder(context)
                                        builder.setMessage("¿Vincular membresía?")
                                            .setPositiveButton("Sí", dialogClickListener)
                                            .setNegativeButton("No", dialogClickListener).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }; return view
    }

}