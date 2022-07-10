package xherrera.esan.dpa_gym_is_life.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import xherrera.esan.dpa_gym_is_life.R
import xherrera.esan.dpa_gym_is_life.data.entities.AssistanceModel
import java.time.LocalDate
import kotlin.random.Random


class AssistanceFragment : Fragment() {

    val args: MenuFragmentArgs by navArgs()

    private lateinit var assistanceView: View
    private lateinit var btnQrScan: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        assistanceView = inflater.inflate(R.layout.fragment_assistance, container, false)

        btnQrScan = assistanceView.findViewById(R.id.btn_qr_scan)
        setBtnQrScanListener()

        return assistanceView
    }

    private fun setBtnQrScanListener(){
        btnQrScan.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this.activity)
            intentIntegrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.QR_CODE))
            intentIntegrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if (result != null){
            val text = result.contents
            if (text.length > 10){
                if (text.substring(0, 8) == "6ym1sLi43"){
                    val db = FirebaseFirestore.getInstance()
                    val dni = args.userId
                    val currentDate = LocalDate.now()
                    val newAssistance = AssistanceModel(dni, currentDate.toString())
                    val docId = Random(3).nextInt().toString()

                    db.collection("assistance")
                        .document(docId)
                        .set(newAssistance)
                        .addOnSuccessListener {
                            Snackbar.make(
                                assistanceView,
                                "Se registró la asistencia",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }
    }

}