package xherrera.esan.dpa_gym_is_life.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import xherrera.esan.dpa_gym_is_life.R

class MenuFragment : Fragment() {

    private lateinit var menuView: View
    private lateinit var btnAssistance: Button
    private lateinit var btnActivities: Button
    private lateinit var btnRrss: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        menuView = inflater.inflate(R.layout.fragment_menu, container, false)

        btnAssistance = menuView.findViewById(R.id.btn_menu_assistance)
        btnActivities = menuView.findViewById(R.id.btn_menu_activities)
        btnRrss = menuView.findViewById(R.id.btn_menu_rrss)

        setBtnAssistanceListener()
        setBtnActivitiesListener()
        setBtnRrssListener()

        return menuView
    }

    private fun setBtnAssistanceListener(){
        btnAssistance.setOnClickListener {
            findNavController()
                .navigate(MenuFragmentDirections
                    .actionMenuFragmentToAssistanceFragment())
        }
    }

    private fun setBtnActivitiesListener(){
        btnActivities.setOnClickListener {
            findNavController()
                .navigate(MenuFragmentDirections
                    .actionMenuFragmentToGroupActivitiesFragment())
        }
    }

    private fun setBtnRrssListener(){
        btnRrss.setOnClickListener {
            Toast.makeText(
                this.context,
                "PRÓXIMAMENTE REDES SOCIALES",
                Toast.LENGTH_LONG).show()
        }
    }

}