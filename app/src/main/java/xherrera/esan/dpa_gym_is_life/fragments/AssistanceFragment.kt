package xherrera.esan.dpa_gym_is_life.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import xherrera.esan.dpa_gym_is_life.R


class AssistanceFragment : Fragment() {

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
            Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
        }
    }

}